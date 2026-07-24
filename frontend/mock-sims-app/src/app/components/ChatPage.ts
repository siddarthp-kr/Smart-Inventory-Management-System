import {
  AfterViewChecked,
  ChangeDetectorRef,
  Component,
  ElementRef,
  inject,
  OnInit,
  ViewChild
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth';
import { Api } from '../services/api';
import { ChatService } from '../services/chat';
import { MarkdownPipe } from '../pipes/markdown.pipe';

@Component({
  selector: 'app-chat-page',
  standalone: true,
  imports: [CommonModule, FormsModule, MarkdownPipe],
  templateUrl: './template/ChatPageTemplate.html'
})
export class ChatPage implements OnInit, AfterViewChecked {
  private auth = inject(AuthService);
  private api = inject(Api);
  protected chatService = inject(ChatService);

  @ViewChild('messageThread') private messageThread!: ElementRef<HTMLDivElement>;

  isNotLoggedIn: boolean = false;
  inputText: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';

  private shouldScrollToBottom = false;

  constructor(private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    const user = this.auth.user();
    if (!user) {
      this.isNotLoggedIn = true;
      return;
    }
    this.chatService.loadForUser(user.storeNumber, user.divisionNumber, user.userEuid);
    this.shouldScrollToBottom = true;
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  private scrollToBottom(): void {
    try {
      const el = this.messageThread?.nativeElement;
      if (el) el.scrollTop = el.scrollHeight;
    } catch {}
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  async sendMessage(): Promise<void> {
    const text = this.inputText.trim();
    if (!text || this.isLoading) return;

    const user = this.auth.user();
    if (!user) {
      this.isNotLoggedIn = true;
      return;
    }

    this.chatService.addMessage({ role: 'user', text });
    this.inputText = '';
    this.isLoading = true;
    this.errorMessage = '';
    this.shouldScrollToBottom = true;
    this.cd.detectChanges();

    try {
      const response = await this.api.agentQuery(
        user.storeNumber,
        user.divisionNumber,
        text,
        this.chatService.conversationId()
      );

      this.chatService.setConversationId(response.conversationId);
      this.chatService.addMessage({ role: 'agent', text: response.summary });
    } catch (error) {
      this.errorMessage = 'Failed to reach the AI assistant. Please try again.';
      console.error('Agent query failed:', error);
    }

    this.isLoading = false;
    this.shouldScrollToBottom = true;
    this.cd.detectChanges();
  }

  clearChat(): void {
    this.chatService.clearChat();
    this.errorMessage = '';
  }
}

