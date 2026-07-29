package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.*;
import mocksims.project.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Stateless Spring component containing all @Tool methods available to the AI agent.
 * Each tool wraps a read-only repository call. Parameters are supplied by the AI model
 * based on context provided in the system prompt.
 */
@Component
public class AgentTools {
    private static final Logger LOG = LoggerFactory.getLogger(AgentTools.class);

    private final BohRepository bohRepository;
    private final MovementInfoRepository movementInfoRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final GetPdmAlertsRepository getPdmAlertsRepository;
    private final ProductsRepository productsRepository;
    private final MarkdownItemRepository markdownItemRepository;

    public AgentTools(
            BohRepository bohRepository,
            MovementInfoRepository movementInfoRepository,
            OrderHistoryRepository orderHistoryRepository,
            GetPdmAlertsRepository getPdmAlertsRepository,
            ProductsRepository productsRepository,
            MarkdownItemRepository markdownItemRepository
    ) {
        this.bohRepository = bohRepository;
        this.movementInfoRepository = movementInfoRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.getPdmAlertsRepository = getPdmAlertsRepository;
        this.productsRepository = productsRepository;
        this.markdownItemRepository = markdownItemRepository;
    }

    @Tool(description = """
            Get balance on hand (BOH) inventory quantities for all products in a store and division.
            Returns upc number, product name, department, quantity on display (qod), and quantity on merchandise (qom).
            """)
    public List<BohItem> getBohInfo(String storeNumber, String divisionNumber) {
        LOG.info("Agent tool: getBohInfo for store {} division {}", storeNumber, divisionNumber);
        return bohRepository.getBohInfo(storeNumber, divisionNumber);
    }

    @Tool(description = """
            Get movement (transaction history) records for a specific product in a store and division.
            Pass the specific UPC number to filter by product. Returns movement type, quantity changed, timestamps, and pricing details.
            """)
    public List<MovementInfoRecord> getMovementInfo(String storeNumber, String divisionNumber, String upcNumber) {
        LOG.info("Agent tool: getMovementInfo for store {} division {} upc {}", storeNumber, divisionNumber, upcNumber);
        return movementInfoRepository.getMovementInfo(storeNumber, divisionNumber, upcNumber);
    }

    @Tool(description = """
            Get the full order history for a store and division.
            Returns past orders with product and quantity information.
            """)
    public List<OrderHistoryRecord> getOrderHistory(String storeNumber, String divisionNumber) {
        LOG.info("Agent tool: getOrderHistory for store {} division {}", storeNumber, divisionNumber);
        OrderHistoryRequest request = new OrderHistoryRequest(storeNumber, divisionNumber);
        return orderHistoryRepository.getOrderHistory(request);
    }

    @Tool(description = """
            Get all active PDM (Product Date Management) alerts for a store and division.
            Returns alert records indicating products that need pricing attention.
            """)
    public List<GetPdmAlertRecord> getPdmAlerts(String storeNumber, String divisionNumber) {
        LOG.info("Agent tool: getPdmAlerts for store {} division {}", storeNumber, divisionNumber);
        return getPdmAlertsRepository.getPdmAlerts(storeNumber, divisionNumber);
    }

    @Tool(description = """
            Get all products available in a store and division.
            Returns UPC numbers and product names. Use this to look up UPC numbers before calling other tools.
            """)
    public List<ProductItem> getProducts(String storeNumber, String divisionNumber) {
        LOG.info("Agent tool: getProducts for store {} division {}", storeNumber, divisionNumber);
        return productsRepository.getProducts(storeNumber, divisionNumber);
    }

    @Tool(description = """
            Get the count of active PDM alerts for a store and division.
            Use this for a quick summary without fetching full alert details.
            """)
    public Integer getPdmAlertCount(String storeNumber, String divisionNumber) {
        LOG.info("Agent tool: getPdmAlertCount for store {} division {}", storeNumber, divisionNumber);
        return getPdmAlertsRepository.getPdmAlertCount(storeNumber, divisionNumber);
    }

    @Tool(description = """
            Get the standard retail price for a product in USD. Use this when calculating shrink (dollars lost).
            """)
    public Double getStandardPrice(String upcNumber) {
        LOG.info("Agent tool: getStandardPrice for upc {}", upcNumber);
        return markdownItemRepository.getStandardPrice(upcNumber);
    }


}
