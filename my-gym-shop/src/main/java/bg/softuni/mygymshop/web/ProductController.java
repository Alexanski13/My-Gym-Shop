package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.CreateProductDTO;
import bg.softuni.mygymshop.model.dtos.ProductDetailDTO;
import bg.softuni.mygymshop.model.dtos.ProductInventoryDTO;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.views.ProductDetailsViewDTO;
import bg.softuni.mygymshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public String getAllProducts(Model model,
                                 @PageableDefault(
                                         sort = "productId",
                                         size = 3
                                 ) Pageable pageable) {

        model.addAttribute("products", productService.getAllProducts(pageable));

        return "products";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        if (!model.containsAttribute("addProductModel")) {
            model.addAttribute("addProductModel", new CreateProductDTO());
        }

        return "product-add";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addProduct(@Valid CreateProductDTO productDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addProductModel", productDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addProductModel",
                    bindingResult);
            return "redirect:/products/add";
        }

        productService.addProduct(productDTO);

        return "redirect:/products/all";
    }

    @GetMapping("/details/{id}")
    public String getProductDetail(@PathVariable("id") Long id, Model model) {
        ProductDetailDTO productDetailDTO = productService.findProductById(id)
                .orElseThrow();
        model.addAttribute("product", productDetailDTO);
        return "details";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        ProductEntity product = productService.getProductById(id);
        CreateProductDTO productDTO = new CreateProductDTO();
        productDTO
                .setId(product.getProductId())
                .setDescription(product.getDescription())
                .setType(product.getType())
                .setName(product.getName())
                .setQuantity(product.getQuantity())
                .setPrice(product.getPrice())
                .setImageUrl(product.getImageUrl());
        model.addAttribute("productDTO", productDTO);
        return "product-edit";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String processUpdateForm(@PathVariable("id") Long id, @ModelAttribute("productDTO") CreateProductDTO productDTO) {
        productService.updateProduct(productDTO);
        return "redirect:/products/details/" + id;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable("id") Long id) {
        productService.deleteProductById(id);

        return "redirect:/products/all";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public String getProductStatus(Model model) {
        List<ProductInventoryDTO> products = productService.getAllProductInventories();
        model.addAttribute("products", products);
        return "product-status";
    }
}
