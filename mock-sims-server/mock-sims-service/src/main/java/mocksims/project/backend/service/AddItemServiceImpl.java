package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.AddItemRequest;
import mocksims.project.backend.api.domain.AddItemResponse;
import mocksims.project.backend.repository.AddItemRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class AddItemServiceImpl implements AddItemService{
    private static final Logger LOG = LoggerFactory.getLogger(AddItemServiceImpl.class);
    private final AddItemRepository addItemRepository;

    /**
     *
     * @param addItemRepository Insert into the database
     */
    public AddItemServiceImpl(AddItemRepository addItemRepository){
        this.addItemRepository = addItemRepository;
    }

    /**
     * Insert data into MARKDOWN_RULES, PRODUCT_BASIC_INFO, and PRODUCT_BOH_INFO in this specific order
     * Method is transactional where all inserts either succeed or rollback if fails
     * @param addItemRequest request the item details, pricing, markdown rules, and store info
     * @return response code
     */
    @Override
    @Transactional
    public AddItemResponse addItem(AddItemRequest addItemRequest){
        AddItemResponse response = new AddItemResponse();
    // Insert each in specific order
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
            LOG.error("Error adding item", error);
            // Set transaction for rollback to prevent insertion
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            response.setResponseCode(500);
            response.setResponseMessage("Error: Failed to add item");
        }
        return response;
    }
}
