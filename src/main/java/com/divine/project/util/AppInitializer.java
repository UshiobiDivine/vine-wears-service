package com.divine.project.util;
import com.divine.project.model.Category;
import com.divine.project.model.Color;
import com.divine.project.model.Size;
import com.divine.project.model.Tag;
import com.divine.project.model.user.Role;
import com.divine.project.model.user.RoleEnum;
import com.divine.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AppInitializer implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private CategoryRepository categoryRepository;
    private ColorRepository colorRepository;
    private SizeRepository sizeRepository;
    private TagRepository tagRepository;


    @Autowired
    public AppInitializer(RoleRepository roleRepository, CategoryRepository categoryRepository, ColorRepository colorRepository, SizeRepository sizeRepository, TagRepository tagRepository) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Role role;
        if (roleRepository.findByName(RoleEnum.ROLE_ADMIN).isEmpty()) {
            role = new Role();
            role.setName(RoleEnum.ROLE_ADMIN);
            roleRepository.save(role);
        }
        if (roleRepository.findByName(RoleEnum.ROLE_USER).isEmpty()) {
            role = new Role();
            role.setName(RoleEnum.ROLE_USER);
            roleRepository.save(role);
        }

//        TOP SELECTIONS
//        NEW ARRIVALS
//        FLASH DEALS
//        JUST FOR YOU

        Tag topSelections = new Tag("Top Selections", "topSelections", "topSelections");
        Tag newArrivals = new Tag("New Arrivals", "newArrivals", "newArrivals");
        Tag flashDeals = new Tag("Flash Deals", "flashDeals", "flashDeals");
        Tag justForYou = new Tag("Just For You", "justForYou", "justForYou");

        Category mens = new Category("Mens", "mens", "https://i.pinimg.com/474x/75/96/84/759684121299ede86081ebb25da91c3d.jpg",
                "shop/mens");

        Category womens = new Category("Womens", "womens", "https://i.ibb.co/GCCdy8t/womens.png",
                "shop/womens");

        Category jackets = new Category("Jackets", "jackets", "https://i.ibb.co/px2tCc3/jackets.png",
                "shop/jackets");

        Category hats = new Category("Hats", "hats", "https://i.ibb.co/cvpntL1/hats.png",
                "shop/hats");

        Category sneakers = new Category("Sneakers", "sneakers", "https://i.ibb.co/0jqHpnp/sneakers.png",
                "shop/sneakers");

        List<Tag> tagList = List.of(topSelections, newArrivals, flashDeals, justForYou);

        List<Category> categoryList = List.of(mens, womens, hats, jackets, sneakers);
        categoryList.forEach(category -> {
            if (categoryRepository.findCategoryByTitle(category.getTitle())==null){
                categoryRepository.save(category);
            }
        });

        tagList.forEach(tag -> {
            if (tagRepository.findByTitle(tag.getTitle())==null){
                tagRepository.save(tag);
            }
        });

    Color red = new Color("red");
    Color white = new Color("white");
    Color black = new Color("black");
    Color blue = new Color("blue");
    Color green = new Color("green");

    List<Color> colorList = List.of(red, white, black, blue, green);
    colorList.forEach(color -> {
        if (colorRepository.findByName(color.getName()).isEmpty()){
            colorRepository.save(color);
        }
    });

    Size S = new Size("s");
    Size M = new Size("m");
    Size L = new Size("l");
    Size XL = new Size("xl");
    Size XXL = new Size("xxl");

    List<Size> sizeList = List.of(S, M, L, XL, XXL);

        sizeList.forEach(size -> {
        if (sizeRepository.findByName(size.getName()).isEmpty()){
            sizeRepository.save(size);
        }
    });

    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

}