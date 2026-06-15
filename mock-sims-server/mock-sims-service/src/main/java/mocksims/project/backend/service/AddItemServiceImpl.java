package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.AddItemRequest;
import mocksims.project.backend.api.domain.AddItemResponse;
import mocksims.project.backend.repository.AddItemRepository;
import mocksims.project.backend.repository.AddItemRepositoryImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddItemServiceImpl implements AddItemService{
    private final AddItemRepository addItemRepository;

    public AddItemServiceImpl(AddItemRepository addItemRepository){
        this.addItemRepository = addItemRepository;
    }

    @Override
    @Transactional
    public AddItemResponse addItem(AddItemRequest addItemRequest){
        AddItemResponse response = new AddItemResponse();

        try{
            addItemRepository.insertMarkdownRules(
                    addItemRequest.getSubcommodityNumber(),
                    addItemRequest.getFirstMarkdownPercent(),
                    addItemRequest.isCanBeMarkedDown(),
                    addItemRequest.getDaysBeforeExpToMD(),
                    addItemRequest.getDaysBeforeExpToRFI(),
                    addItemRequest.getDaysAfterOrderToSetExp()
            );

            addItemRepository.insertProductBasicInfo(
                    addItemRequest.getUpcNumber(),
                    addItemRequest.getSubcommodityNumber(),
                    addItemRequest.getDepartmentNumber(),
                    addItemRequest.getProductName(),
                    addItemRequest.getStandardPrice()
            );

            addItemRepository.insertProductBohInfo(
                    addItemRequest.getDivisionNumber(),
                    addItemRequest.getStoreNumber(),
                    addItemRequest.getUpcNumber()
            );

            response.setResponseCode(200);
            response.setResponseMessage("Item added successfully");

        } catch (DataAccessException error){
            System.out.println("Error adding item");
            response.setResponseCode(500);
            response.setResponseMessage("Error: Failed to add item");

            // Allows for rollback
            throw error;
        }
        return response;
    }
}
