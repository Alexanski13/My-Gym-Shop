package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.CreateProductDTO;
import bg.softuni.mygymshop.model.dtos.ProductDetailDTO;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.service.ProductService;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping("/{id}")
//    public String getProductById(@PathVariable("id") Long productId) {
//        //todo
//        return "details";
//    }

    @GetMapping("/all")
    public String getAllOffers(Model model,
                               @PageableDefault(
                                       sort = "productId",
                                       size = 3
                               ) Pageable pageable) {

        var allProductsPage = productService.getAllProducts(pageable);

        model.addAttribute("products", allProductsPage);

        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        if (!model.containsAttribute("addProductModel")) {
            model.addAttribute("addProductModel", new CreateProductDTO());
        }

        return "product-add";
    }

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

    @GetMapping("/{id}")
    public String getProductDetail(@PathVariable("id") Long id, Model model) {
        ProductDetailDTO productDetailDTO = productService.findProductById(id)
                .orElseThrow();
        model.addAttribute("product", productDetailDTO);
        return "details";
    }


}
