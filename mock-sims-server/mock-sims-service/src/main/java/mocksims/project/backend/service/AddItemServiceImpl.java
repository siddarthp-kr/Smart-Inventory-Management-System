package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.AddItemRequest;
import mocksims.project.backend.api.domain.AddItemResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.AddItemRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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

            if (!addItemRepository.markdownRuleExists(addItemRequest.getSubcommodityNumber())) {
                addItemRepository.insertMarkdownRules(
                        addItemRequest.getSubcommodityNumber(),
                        addItemRequest.getFirstMarkdownPercent(),
                        addItemRequest.isCanBeMarkedDown(),
                        addItemRequest.getDaysBeforeExpToMD(),
                        addItemRequest.getDaysBeforeExpToRFI(),
                        addItemRequest.getDaysAfterOrderToSetExp()
                );
            }


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

        } catch (DuplicateKeyException error){
            LOG.error("Add Item failed: duplicate key detected. UPC already exists.", error);
            // Set transaction for rollback to prevent insertion
            throw new MockSimsCustomException(500, "Error: Failed to add item - UPC already exists");
        } catch (DataIntegrityViolationException error){
            LOG.error("Add Item Failed: insert prevented for requested item. (Database integrity rule)", error);
            throw new MockSimsCustomException(500, "Error: Failed to add item - Database integrity rule prevented insert");
        } catch (DataAccessException error){
            LOG.error("Add Item failed: database error.", error);
            throw new MockSimsCustomException(500, "Error: Failed to add item");
        }
        return response;
    }
}
