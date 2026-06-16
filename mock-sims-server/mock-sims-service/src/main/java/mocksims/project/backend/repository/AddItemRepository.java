package mocksims.project.backend.repository;

public interface AddItemRepository {
    boolean markdownRuleExists(String subcommodityNumber);

    void insertMarkdownRules(String subcommodityNumber, int firstMarkdownPercent, boolean canBeMarkedDown, int daysBeforeExpToMD, int daysBeforeExpToRFI, int daysAfterOrderToSetExp);

    void insertProductBasicInfo(String upcNumber, String subcommodityNumber, String departmentNumber, String productName, double standardPrice);

    void insertProductBohInfo(String divisionNumber, String storeNumber, String upcNumber);


}
