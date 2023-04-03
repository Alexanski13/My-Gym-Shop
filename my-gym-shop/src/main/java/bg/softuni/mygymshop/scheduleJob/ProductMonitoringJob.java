package bg.softuni.mygymshop.scheduleJob;

import bg.softuni.mygymshop.model.dtos.ProductInventoryDTO;
import bg.softuni.mygymshop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMonitoringJob {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductMonitoringJob.class);

    private final ProductService productService;

    @Autowired
    public ProductMonitoringJob(ProductService productService) {
        this.productService = productService;
    }

    @Scheduled(fixedRate = 5000)
    public void monitorProducts() {
        List<ProductInventoryDTO> productInventories = productService.getAllProductInventories();
        productService.updateProductInventories(productInventories);
    }
}
