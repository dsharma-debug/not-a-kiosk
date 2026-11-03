import java.util.ArrayList;
public class SampleCatalog {
    public static ArrayList<Department> createDepartments() {
        ArrayList<Department> departments = new ArrayList<Department>();
        addDepartment(departments, "D1", "Grocery and Snacks", new String[][] {
                {"G101", "Olive Oil", "Extra virgin olive oil for cooking, salad dressing, pasta, oily food, pantry, healthy fats", "9.99", "18"},
                {"G102", "Jasmine Rice", "Large bag of jasmine rice for dinner, meal prep, family meals, cheap staple, pantry", "12.49", "24"},
                {"G103", "Spicy Ramen Pack", "Instant ramen with spicy broth, noodles, quick meal, hot food, cheap lunch", "5.99", "40"},
                {"G104", "Vegan Protein Bars", "Plant based snack bars with chocolate flavor, vegan, gym, school, quick breakfast", "7.49", "30"},
                {"G105", "Organic Apples", "Fresh red apples, fruit, healthy snack, lunchbox, sweet, produce", "4.99", "35"},
                {"G106", "Peanut Butter", "Creamy peanut butter spread, protein, sandwich, breakfast, oily texture, pantry", "4.79", "22"}
        });
        addDepartment(departments, "D2", "Drinks", new String[][] {
                {"D201", "Cola", "Cold coca cola style soda, sweet drink, caffeine, party, cheap drink", "2.49", "50"},
                {"D202", "Sparkling Water", "Lime sparkling water, no sugar, refreshing drink, fizzy, healthy", "3.99", "42"},
                {"D203", "Iced Coffee", "Ready to drink iced coffee, caffeine, breakfast, cold drink, sweet", "4.49", "20"},
                {"D204", "Orange Juice", "Fresh orange juice with vitamin C, breakfast drink, fruit, family size", "5.99", "16"},
                {"D205", "Electrolyte Drink", "Sports hydration drink for gym, workout, running, recovery, energy", "3.49", "28"}
        });
        addDepartment(departments, "D3", "Personal Care", new String[][] {
                {"P301", "Clarifying Shampoo", "Deep cleansing shampoo for oily hair, scalp care, haircare, shower, fresh scent", "8.49", "14"},
                {"P302", "Moisturizing Conditioner", "Conditioner for dry hair, soft hair, smooth, haircare, shower, repair", "8.49", "13"},
                {"P303", "Tea Tree Face Wash", "Face cleanser for oily skin, acne care, refreshing, skincare, bathroom", "6.99", "18"},
                {"P304", "Body Lotion", "Hydrating lotion for dry skin, fragrance free, skincare, winter, sensitive skin", "7.99", "12"},
                {"P305", "Toothpaste", "Mint toothpaste for clean teeth, oral care, bathroom, fresh breath", "3.29", "36"},
                {"P306", "Sunscreen SPF 50", "Lightweight sunscreen for face and body, summer, beach, skincare, non oily", "10.99", "11"}
        });
        addDepartment(departments, "D4", "Clothing and Apparel", new String[][] {
                {"C401", "Vegan Leather Jacket", "Black vegan leather jacket, fashion, apparel, outerwear, stylish, rain resistant", "49.99", "6"},
                {"C402", "Cotton T-Shirt", "Soft cotton t-shirt, casual clothing, basic, cheap apparel, everyday", "12.99", "26"},
                {"C403", "Rain Hoodie", "Water resistant hoodie for rainy weather, jacket, school, outdoor, warm", "34.99", "9"},
                {"C404", "Running Socks", "Breathable athletic socks for gym, walking, running, comfort, sports", "6.99", "32"},
                {"C405", "Winter Beanie", "Warm knit beanie hat for cold weather, winter, apparel, cozy", "9.99", "15"}
        });
        addDepartment(departments, "D5", "Home and Cleaning", new String[][] {
                {"H501", "Dish Soap", "Lemon dish soap cuts oily dishes and grease, kitchen cleaning, sink, fresh", "3.99", "30"},
                {"H502", "Laundry Detergent", "Fresh laundry detergent for clothes, cleaning, home, family, stain removal", "11.99", "17"},
                {"H503", "Paper Towels", "Absorbent paper towels for spills, cleaning, kitchen, household, bulk", "8.99", "25"},
                {"H504", "All Purpose Cleaner", "Spray cleaner for counters, bathroom, kitchen, grease, oily mess, home", "4.99", "21"},
                {"H505", "Trash Bags", "Strong kitchen trash bags, home supplies, cleaning, large size", "7.99", "19"}
        });
        addDepartment(departments, "D6", "Electronics and School", new String[][] {
                {"E601", "Phone Charger", "USB-C fast phone charger, electronics, travel, school, office", "14.99", "20"},
                {"E602", "Wireless Earbuds", "Bluetooth earbuds for music, school, gym, calls, electronics", "29.99", "8"},
                {"E603", "Notebook", "College ruled notebook for school, notes, office, writing, cheap supplies", "2.99", "45"},
                {"E604", "Gel Pens", "Smooth gel pens for school, office, colorful writing, notes", "4.49", "34"},
                {"E605", "Desk Lamp", "LED desk lamp for study, office, bedroom, school, adjustable light", "19.99", "10"}
        });
        return departments;
    }
    private static void addDepartment(ArrayList<Department> departments,
                                      String code,
                                      String name,
                                      String[][] productRows) {
        Department department = new Department(code, name);
        for (int i = 0; i < productRows.length; i++) {
            String[] row = productRows[i];
            department.addProduct(new Product(
                    row[0],
                    row[1],
                    row[2],
                    Double.parseDouble(row[3]),
                    Integer.parseInt(row[4])
            ));
        }
        departments.add(department);
    }
}
