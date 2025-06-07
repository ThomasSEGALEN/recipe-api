package com.globalopencampus.recipeapi.config;

import com.globalopencampus.recipeapi.entity.*;
import com.globalopencampus.recipeapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@recipeapi.com", passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        // Create regular user if not exists
        if (!userRepository.existsByUsername("user")) {
            User user = new User("user", "user@recipeapi.com", passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);
        }

        // Create categories if they don't exist
        createCategoryIfNotExists("Italian", "Traditional Italian cuisine recipes");
        createCategoryIfNotExists("Mexican", "Authentic Mexican dishes and flavors");
        createCategoryIfNotExists("Asian", "Various Asian cuisine styles and recipes");
        createCategoryIfNotExists("American", "Classic American comfort food");
        createCategoryIfNotExists("Desserts", "Sweet treats and dessert recipes");
        createCategoryIfNotExists("Vegetarian", "Plant-based and vegetarian recipes");
        createCategoryIfNotExists("Breakfast", "Morning meals and brunch ideas");
        createCategoryIfNotExists("Appetizers", "Starters and small plate recipes");

        // Create sample recipes if database is empty
        if (recipeRepository.count() == 0) {
            createSampleRecipes();
        }
    }

    private void createCategoryIfNotExists(String name, String description) {
        if (!categoryRepository.existsByName(name)) {
            Category category = new Category(name, description);
            categoryRepository.save(category);
        }
    }

    private void createSampleRecipes() {
        User user = userRepository.findByUsername("user").orElse(null);
        if (user == null) return;

        // Sample Recipe 1: Spaghetti Carbonara
        Category italian = categoryRepository.findByName("Italian").orElse(null);
        if (italian != null) {
            Recipe carbonara = new Recipe();
            carbonara.setTitle("Spaghetti Carbonara");
            carbonara.setDescription("Classic Italian pasta dish with eggs, cheese, and pancetta");
            carbonara.setInstructions("1. Cook spaghetti in salted water until al dente\n2. In a bowl, whisk eggs with grated Parmesan\n3. Cook pancetta until crispy\n4. Drain pasta, reserving pasta water\n5. Mix hot pasta with egg mixture, adding pasta water gradually\n6. Add pancetta and season with pepper\n7. Serve immediately with extra cheese");
            carbonara.setPreparationTime(15);
            carbonara.setCookingTime(20);
            carbonara.setServings(4);
            carbonara.setDifficulty(Difficulty.MEDIUM);
            carbonara.setUser(user);
            carbonara.setCategory(italian);
            Recipe savedCarbonara = recipeRepository.save(carbonara);

            // Add ingredients - UTILISE LE BON CONSTRUCTEUR
            ingredientRepository.save(new Ingredient("Spaghetti", 400.0, "g", savedCarbonara));
            ingredientRepository.save(new Ingredient("Eggs", 4.0, "pieces", savedCarbonara));
            ingredientRepository.save(new Ingredient("Parmesan cheese", 100.0, "g", savedCarbonara));
            ingredientRepository.save(new Ingredient("Pancetta", 150.0, "g", savedCarbonara));
            ingredientRepository.save(new Ingredient("Black pepper", 1.0, "tsp", savedCarbonara));
        }

        // Sample Recipe 2: Chocolate Chip Cookies
        Category desserts = categoryRepository.findByName("Desserts").orElse(null);
        if (desserts != null) {
            Recipe cookies = new Recipe();
            cookies.setTitle("Chocolate Chip Cookies");
            cookies.setDescription("Soft and chewy homemade chocolate chip cookies");
            cookies.setInstructions("1. Preheat oven to 375°F (190°C)\n2. Cream butter and sugars together\n3. Beat in eggs and vanilla\n4. Mix in flour, baking soda, and salt\n5. Fold in chocolate chips\n6. Drop spoonfuls onto baking sheet\n7. Bake 9-11 minutes until golden brown\n8. Cool on baking sheet for 5 minutes");
            cookies.setPreparationTime(20);
            cookies.setCookingTime(11);
            cookies.setServings(24);
            cookies.setDifficulty(Difficulty.EASY);
            cookies.setUser(user);
            cookies.setCategory(desserts);
            Recipe savedCookies = recipeRepository.save(cookies);

            // Add ingredients
            ingredientRepository.save(new Ingredient("All-purpose flour", 2.25, "cups", savedCookies));
            ingredientRepository.save(new Ingredient("Butter", 1.0, "cup", savedCookies));
            ingredientRepository.save(new Ingredient("Brown sugar", 0.75, "cup", savedCookies));
            ingredientRepository.save(new Ingredient("White sugar", 0.75, "cup", savedCookies));
            ingredientRepository.save(new Ingredient("Eggs", 2.0, "pieces", savedCookies));
            ingredientRepository.save(new Ingredient("Vanilla extract", 2.0, "tsp", savedCookies));
            ingredientRepository.save(new Ingredient("Baking soda", 1.0, "tsp", savedCookies));
            ingredientRepository.save(new Ingredient("Salt", 1.0, "tsp", savedCookies));
            ingredientRepository.save(new Ingredient("Chocolate chips", 2.0, "cups", savedCookies));
        }

        // Sample Recipe 3: Guacamole
        Category mexican = categoryRepository.findByName("Mexican").orElse(null);
        if (mexican != null) {
            Recipe guacamole = new Recipe();
            guacamole.setTitle("Fresh Guacamole");
            guacamole.setDescription("Creamy and flavorful homemade guacamole");
            guacamole.setInstructions("1. Cut avocados in half and remove pits\n2. Scoop flesh into a bowl\n3. Mash avocados with a fork, leaving some chunks\n4. Add lime juice, salt, and cumin\n5. Fold in diced onion, tomato, and jalapeño\n6. Add cilantro and garlic\n7. Taste and adjust seasoning\n8. Serve immediately with tortilla chips");
            guacamole.setPreparationTime(15);
            guacamole.setCookingTime(0);
            guacamole.setServings(6);
            guacamole.setDifficulty(Difficulty.EASY);
            guacamole.setUser(user);
            guacamole.setCategory(mexican);
            Recipe savedGuacamole = recipeRepository.save(guacamole);

            // Add ingredients
            ingredientRepository.save(new Ingredient("Ripe avocados", 3.0, "pieces", savedGuacamole));
            ingredientRepository.save(new Ingredient("Lime juice", 2.0, "tbsp", savedGuacamole));
            ingredientRepository.save(new Ingredient("Salt", 0.5, "tsp", savedGuacamole));
            ingredientRepository.save(new Ingredient("Cumin", 0.25, "tsp", savedGuacamole));
            ingredientRepository.save(new Ingredient("White onion", 0.25, "cup", savedGuacamole));
            ingredientRepository.save(new Ingredient("Roma tomato", 1.0, "piece", savedGuacamole));
            ingredientRepository.save(new Ingredient("Jalapeño", 1.0, "piece", savedGuacamole));
            ingredientRepository.save(new Ingredient("Cilantro", 2.0, "tbsp", savedGuacamole));
            ingredientRepository.save(new Ingredient("Garlic", 1.0, "clove", savedGuacamole));
        }
    }
}