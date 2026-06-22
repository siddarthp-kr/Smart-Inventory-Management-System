package mocksims.project.backend.util;

import mocksims.project.backend.api.domain.PlaceOrderItem;
import mocksims.project.backend.controller.PlaceOrderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ValidationHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationHelper.class);

    /*
        Returns true if the UPC number is valid
     */
    public static boolean validateUpcNumber(String upc){
        boolean isValid = true;

        if(upc == null){
            isValid = false;
        } else if(upc.length() > 15 || upc.length() < 4){
            isValid = false;
        } else if(!canParseInt(upc)){
            isValid = false;
        }

        return isValid;
    }

    /*
        Returns true if the Store Number is valid
     */
    public static boolean validateStoreNumber(String storeNumber){
        boolean isValid = true;
        if(storeNumber == null){
            isValid = false;
        } else if(storeNumber.length() != 5){
            isValid = false;
        } else if(!canParseInt(storeNumber)){
            isValid = false;
        }
        return isValid;
    }

    /*
        Returns true if the Division Number is valid
     */
    public static boolean validateDivisionNumber(String divisionNumber){
        boolean isValid = true;
        if(divisionNumber == null){
            isValid = false;
        } else if(divisionNumber.length() != 3){
            isValid = false;
        } else if(!canParseInt(divisionNumber)){
            isValid = false;
        }
        return isValid;
    }

    /*
        Returns true if EUID is valid
     */
    public static boolean validateUserEuid(String euid){
        boolean isValid = true;

        isValid = euid != null && euid.length() > 5 && euid.length() < 11;

        return isValid;
    }

    private static boolean canParseInt(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validateOrderItems(List<PlaceOrderItem> items) {

        if(items == null || items.isEmpty()){
            return false;
        } else {
            for(PlaceOrderItem item: items){
                if(! validateUpcNumber(item.getUpcNumber())){
                    LOG.error("Invalid item in order: {} is not a valid UPC.", item.getUpcNumber());
                    return false;
                }
                if (! (item.getQuantity() > 0)){
                    LOG.error("Invalid item in order: Cannot order quantity {} of UPC {}.", item.getQuantity(), item.getUpcNumber());
                    return false;
                }
            }
        }

        return true;
    }
}
