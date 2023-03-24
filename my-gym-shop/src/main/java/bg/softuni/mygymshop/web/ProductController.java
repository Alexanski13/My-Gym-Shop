package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.CreateProductDTO;
import bg.softuni.mygymshop.repository.ProductCategoryRepository;
import bg.softuni.mygymshop.service.ApplicationUserDetailsService;
import bg.softuni.mygymshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final ProductService productService;

    private final ProductCategoryRepository productCategoryRepository;

    public ProductController(ProductService productService, ProductCategoryRepository productCategoryRepository) {
        this.productService = productService;
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable("id") Long id) {
        //todo
        return "details";
    }

    @GetMapping("/products/all")
    public String getAllOffers(Model model,
                               @PageableDefault(
                                       sort = "productId",
                                       size = 3
                               ) Pageable pageable) {

        var allProductsPage = productService.getAllProducts(pageable);

        model.addAttribute("products", allProductsPage);

        return "products";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model) {
        if (!model.containsAttribute("addProductModel")) {
            model.addAttribute("addProductModel", new CreateProductDTO());
        }

        return "product-add";
    }

    @PostMapping("/products/add")
    public String addOffer(@Valid CreateProductDTO addProductModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal ApplicationUserDetailsService userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addProductModel", addProductModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addProductModel",
                    bindingResult);
            return "redirect:/products/add";
        }

        productService.addProduct(addProductModel);

        return "redirect:/products/all";
    }

}
