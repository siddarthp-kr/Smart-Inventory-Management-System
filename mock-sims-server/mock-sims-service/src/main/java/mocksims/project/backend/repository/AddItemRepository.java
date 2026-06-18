package mocksims.project.backend.repository;

public interface AddItemRepository {
    boolean markdownRuleExists(String subcommodityNumber);

    void insertMarkdownRules(String subcommodityNumber, Integer firstMarkdownPercent, Boolean canBeMarkedDown, Integer daysBeforeExpToMD, Integer daysBeforeExpToRFI, Integer daysAfterOrderToSetExp);

    void insertProductBasicInfo(String upcNumber, String subcommodityNumber, String departmentNumber, String productName, double standardPrice);

    void insertProductBohInfo(String divisionNumber, String storeNumber, String upcNumber);

}
