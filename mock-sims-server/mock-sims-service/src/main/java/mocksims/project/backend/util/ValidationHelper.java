package mocksims.project.backend.util;

public class ValidationHelper {

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

}
