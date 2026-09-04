# Sugar Meter

Works out how many carbohydrates are in a portion of a home-cooked meal. Android application in
Kotlin, 2021.

<p align="center">
  <img src="untitled.gif" alt="Sugar Meter — composing a dish from weighed ingredients" width="300">
</p>

## The problem

Carbohydrate counts on packaging are given per 100 g of the raw ingredient. That is no help once
the ingredients have been cooked into something: a casserole weighs less than the sum of what
went into it, because water boils off, and nobody eats the whole pot anyway. Anyone counting
carbohydrates — for diabetes, most obviously — needs the figure for *the plate in front of them*.

This app closes that gap. Enter the recipe by weight, weigh the finished dish, and it gives you
the carbohydrates in whatever size portion you actually take.

## How it works

The model has two levels:

- **Ingredient types** — a reusable library entry: a name, an optional picture, and grams of
  carbohydrate per 100 g
- **Ingredients** — one of those types with a weight, as used in a particular dish
- **Food** — a named dish made of weighed ingredients, plus its **real weight** after cooking

The interesting part is the last field. Carbohydrates are summed from the raw ingredients, but
divided by the measured weight of the finished dish, which gives carbohydrate per gram of the
actual food. Multiply by the weight of a serving and you have the answer:

```kotlin
fun sugarInOneGramFood(): Double = sumWeightSugar() / realWeight!!

fun weightSugarInPartFood(weightPartFood: Double): Double =
    weightPartFood * (sumWeightSugar() / realWeight!!)
```

## What it does

- Build up a personal library of ingredients with their carbohydrate values
- Compose dishes from weighed ingredients and store them for re-use
- Enter the cooked weight and get carbohydrates per portion
- Look values up on the web in a built-in browser without leaving the app
- Everything is kept on the device, as JSON in `SharedPreferences` — no account, no server

## Implementation notes

| Piece | File |
|---|---|
| Domain model | `Food.kt`, `Ingredient.kt`, `TypeIngredient.kt` |
| Stores, both singletons | `FoodsManager.kt`, `TypeIngredientsManager.kt` |
| Persistence | `JsonParser.kt` |
| Screens | `MainActivity.kt`, `FoodActivity.kt`, `TypeIngredientsListActivity.kt`, `WebActivity.kt` |
| List rendering | the four `*ListAdapter.kt` classes |

Plain Android with activities and `ListView` adapters, validation kept on the model itself
(`isCorrect()` on every class) rather than in the UI.

## Borrowed code

- [Avatar Image Generator](https://github.com/AmosKorir/AvatarImageGenerator)
- [Custom styled toast](https://www.geeksforgeeks.org/how-to-add-a-custom-styled-toast-in-android-using-kotlin/)

## Status

Archived.
