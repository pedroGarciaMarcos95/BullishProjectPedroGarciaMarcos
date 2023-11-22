# Bullish Project Pedro García Marcos

## Requirements

* Java: Version >= 17. _(Command to install it with SDK : sdk install java 17.0.7-sapmchn)_
* Gradle: Version >= 8.4 _(If you have gradle it downloads automatically when you start the project.)_

## Configuration

* The project is already preconfigured.
* The main part of the configuration is done in the build.gradle.

## Set up project

* Clone the repository.
* Run the following command into cloned folder afterward.

```shell
./gradlew
```
## Test execution

* Run the following command into the main folder to execute all tests.

```shell
./gradlew clean test
```

## Init Project

* Run the following command into the main folder to start the project.

```shell
 ./gradlew bootRun
```

* The application should be running now and ready to test through POSTMAN.

## POSTMAN TEST

* You have the possibility to test the application through POSTMAN.
* POSTMAN collection path: resources/postmanCollection/Bullish Project.postman_collection.json . [ POSTMAN COLLECTION](https://github.com/pedroGarciaMarcos95/BullishProjectPedroGarciaMarcos/blob/main/src/main/resources/postmanCollection/Bullish%20Project.postman_collection.json)
* The collection.json is ready to be imported and tested.
* Main endpoints functionality :
  * Product:
    * Add product: Add a product to the product list .
    * Remove product: Remove a product to the product list.
    * Get all products Get all products from the product list.
  * Cart:
    * Add product to cart: Add a product from the product list to the cart. If the cart with this Client iD is empty it is created.
    * Remove all products by id from cart: Remove all products with the same id from a cart.
  * Discount:
    * Create discount: Add a discount to the discount list.
    * Assign discount to a product: Assign discount from the discount list to a cart.
    * Create and assign discount: Add a discount to the discount list and assign it to a cart directly.
  * Order:
    * Purchase cart: Transform a cart to and Order and the cart get deleted. (The order ID will be the previous Cart ID witch is the Client ID).
    * Get all carts by client id: Get all the orders created with the same Client ID.


## DEV NOTES

* As the requirements were not very concrete, I think I have done more than necessary.
* I have left comments around the project explaining some things and why I have done them this way.
* I have prioritized to show you my programming skills over the simplicity of the code.

## Bullish Technical Assessment

* [Technical Assessment](https://github.com/pedroGarciaMarcos95/BullishProjectPedroGarciaMarcos/blob/e9322b733053403449b97a5053b56e9823a6be54/src/main/resources/Bullish%20Technical%20Assessment%20(Take%20home).pdf)
