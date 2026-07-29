import { Injectable, signal } from '@angular/core';

export interface ChatMessage {
  role: 'user' | 'agent';
  text: string;
}

interface ChatState {
  messages: ChatMessage[];
  conversationId: string | null;
}

@Injectable({ providedIn: 'root' })
export class ChatService {
  private storageKey: string | null = null;

  private _messages = signal<ChatMessage[]>([]);
  private _conversationId = signal<string | null>(null);

  readonly messages = this._messages.asReadonly();
  readonly conversationId = this._conversationId.asReadonly();

  /**
   * Must be called each time the ChatPage initialises.
   * Builds the user-scoped storage key and hydrates signals from localStorage.
   */
  loadForUser(storeNumber: string, divisionNumber: string, userEuid: string): void {
    this.storageKey = `simsChat_${storeNumber}_${divisionNumber}_${userEuid}`;
    const saved = localStorage.getItem(this.storageKey);
    try {
      if (saved) {
        const state: ChatState = JSON.parse(saved);
        this._messages.set(state.messages ?? []);
        this._conversationId.set(state.conversationId ?? null);
      } else {
        this._messages.set([]);
        this._conversationId.set(null);
      }
    } catch {
      this._messages.set([]);
      this._conversationId.set(null);
    }
  }

  addMessage(message: ChatMessage): void {
    this._messages.update(msgs => [...msgs, message]);
    this.persist();
  }

  setConversationId(id: string): void {
    this._conversationId.set(id);
    this.persist();
  }

  clearChat(): void {
    this._messages.set([]);
    this._conversationId.set(null);
    if (this.storageKey) {
      localStorage.removeItem(this.storageKey);
    }
  }

  private persist(): void {
    if (!this.storageKey) return;
    const state: ChatState = {
      messages: this._messages(),
      conversationId: this._conversationId()
    };
    localStorage.setItem(this.storageKey, JSON.stringify(state));
  }
}

