# Automated End-to-End User journey tests with Playwright and Cucumber

This repository deals with automated user journey tests. We use playwright for automation and Cucumber and Gherking as the framework to describe the scenarios.
The idea behind this is to allow a colaborative environment where anyone can write down a set operations that composes a Journey to be tested, and the developers will make sure to implement it.

The main objective is to cover as many cases as possible by allowing non developers to describe scenarios that sometimes are forgotten by the development team and allow different point of view for journeys.

The below sections are intended to explain how the framework works, along with a few instructions. Please make sure to read it carefuly and don't be afraid to play around.

## Introduction to Writing Cucumber/Gherkin Files for End-to-End Tests

[Cucumber](https://cucumber.io/) is a popular tool used for behavior-driven development (BDD) in software testing. It allows you to write executable specifications in a natural language format called Gherkin. Gherkin is a plain-text language that is easy to understand and can be used by non-technical stakeholders as well. In this tutorial, we will guide you through the basics of writing a Cucumber/Gherkin file for end-to-end tests. To learn more about Cucumber, Gherking and BDD, you can enroll on a [free course](https://school.cucumber.io/courses/bdd-overview-for-business-analysts-and-product-owners).

It is always good to read about Gherkin language format before start but in summary, Gherkin is a writing format that leverages plain english to allow users to describe the expected behavior in simple steps that is simple to understand by non-technical persons, but also makes sense to be executed in that specific order. Think of it like a set of steps in a cake recipe.

To make things easier for non-technical team members, we developed a strategy to leverage the use of [github issues](https://github.com/bcgov/nr-forest-client/issues), something that is quite similar to Jira Tickets. Click on `New issue` and a list of possible issue types will appear, select the `User provided automated test-case` one, and a form will appear. Follow the instructions on it on how to fill up the form with the appropriate data. This will then be automatically converted to a `feature file` that will be used for test.

    Pay attention to the format of the test description. Gherkin feature files are sensitive to spacing and the keywords are really picky when it comes to casing (uppercase x lowercase).

Another thing that the development team did to facilitate the usage of Gherkin is the ready-to-use collection of instructions that can be used to speed up the writing of test cases. Check the [existing step instructions](#existing-step-instructions) topic for a list of steps already implemented.

Without further ado, let's get started:

### Creating a Feature

Every test group is called a `Feature` on Gherkin. This is the first keyword here and it will have a meaningful name. This will contain your scenarios and it can include a short description of the feature you are testing, preceded by the Feature keyword. For example:

```gherkin
Feature: User Registration
  As a new user I want to register on the website so that I can access exclusive content
```

    Be aware that the description should be a level deeper than the Feature itself. You can use tab or two spaces

### Creating Scenarios

Scenarios represent specific test cases. Each scenario consists of a series of steps. Steps can be one of the following: **Given**, **When**, **Then**, **And**, or **But**. Here's an example scenario for our user registration feature:

```gherkin
Scenario: Successful user registration
  Given I am on the registration page
  When I fill in the registration form with valid information
  And I click the "Register" button
  Then I should see a success message
```

    Be aware that each step should be a level deeper than the Scenario itself. You can use tab or two spaces
    Also, keep in mind that this should be at least one level deeper than the feature above it.

Here's the final product of the above feature and scenario combined.

```gherkin
Feature: User Registration
  As a new user I want to register on the website so that I can access exclusive content
    
  Scenario: Successful user registration
    Given I am on the registration page
    When I fill in the registration form with valid information
    And I click the "Register" button
    Then I should see a success message
```

Congratulations! You have successfully written a basic Cucumber/Gherkin end-to-end tests. You can continue adding more scenarios and step definitions to cover different test cases in the application.

Remember, the power of Cucumber lies in its ability to bridge the communication gap between technical and non-technical team members, enabling collaboration and providing a common language for defining software behavior.

### Existing step instructions {#steplist}

The development team created a set of pre-defined step definitions that can be used to leverage the use of Gherkin tests and speed up the adoption of it with non-technical team members. The below steps should be used `as-is`, without changing the case of any letter, except for `variables`.

A variable is a piece of information that will be used to pass down a information. Every variable should be wrapped in double quotes (`"`). We will have two distinc group of variables described below, and they will be defined as `input` and `field name`. **Input** variables are the actual data that you want to select or insert in the form, such as the first name `James` or the `Individual` type of user. **Field name** is a type of variable used to identify a field in the form based on it's label name. Some examples are `First name` and `Client type`. They should have the exact name and casing as the form, otherwise the test won't be able to find the input to fill the data in.

    The below list of instructions can be used in any of the steps, such as `Given`, `When`, `Then`, `And` or `But`. They are case sensitive and should be used as they are.

Also, to speed up the process and avoid credentials being leaked everywhere, we have a special instruction that will be used to login using some specific user types. This instruction uses the `annotation` and it should be used at the `scenario` level. For more information, please refer to the [credentials](#credentials) topic below.

Here's a list of instructions that are already implemented and can be used:

| Step            | Variables                                         | Description                     | Example           |
| --------------- | ------------------------------------------------- | ------------------------------- | ----------------- |
| I visit {input} | `input` as the URL. The `/` prefix can be ignored | Navigate to a specific URL path | I visit "landing" |
| I can see the page is called {input} | `input` as the page title | Check if the page title matches | I can see the page is called "Dashboard" |
| I can read {input} | `input` as the text to be read | Check if the page contains specific text | I can read "Welcome to the Dashboard" |
| I can see the button {input} | `input` as the button text | Check if the button is visible | I can see the button "Submit" |
| I click on the {input} button | `input` as the button text | Click on a specific button | I click on the "Submit" button |
| I can see the page URL is {input} | `input` as the URL | Check if the page URL matches | I can see the page has the URL "/dashboard" |
| I can see the title {input} | `input` as the title text | Check if the page title (AKA header) matches | I can see the title "Dashboard" |
| I search for {input} | `input` as the search term | Perform a search action, by finding an entry that is a searchbox, filling the text and then pressing enter | I search for "Playwright" |
| I can see {criteria} {quantity} result on the table {name} | `criteria` as the criteria to check the quantity, possible values are `at least`, `at most` and `exactly`, `quantity` as the number of results expected, `name` as the table name | Check if a specific table has a certain number of results with a specific criteria | I can see "at least" 5 result on the table "Users" |

## For Developers

Each test is written in a file with the `.feature` extension and is saved inside [src/features](src/features) folder on this repo, you can check some of the existing files for reference. The developer will implement instructions inside [src/steps](src/steps) inside each group of `.ts` file for each kind of instruction. Ex: `sample.feature` will have a couple of instructions that will be present inside the corresponding `.ts` files.

    Avoid using names with spaces or special characters. Replace spaces with `_` and special characters for it's non-special character counterpart

### Creating a Feature File

Create a new file with the `.feature` extension inside the [src/features](src/features) folder. This file will contain your Gherkin scenarios. Start by writing a short description of the feature you are testing, preceded by the Feature keyword. For example:

```gherkin
Feature: User Registration
  As a new user
  I want to register on the website
  So that I can access exclusive content
```

### Writing Scenarios

Scenarios represent specific test cases. Each scenario consists of a series of steps. Steps can be one of the following: **Given**, **When**, **Then**, **And**, or **But**. Here's an example scenario for our user registration feature:

```gherkin
Scenario: Successful user registration
  Given I am on the registration page
  When I fill in the registration form with valid information
  And I click the "Register" button
  Then I should see a success message
```

### Writing Step Definitions

Step definitions are the code implementation of the Gherkin steps. Unfortunately, playwright doesn't support a direct integration with cucumber/gherkin directly as cypress does, so we have to be a bit more creative when writing steps. Inside the [steps folder](src/steps/) you will find a set of `.ts` files that correspond to the steps defined in your feature files. Each file contains step definitions that match the steps in your Gherkin scenarios. They follow a format defined withing the [StepDefinition interface](src/steps/stepTypes.ts) file, which is a TypeScript interface that defines the structure of a step definition. You will see that we have a function that's used to generate the actual playwright code to be executed, wrapped in string templates. This allows us to write the steps in a more readable way, even though we can't execute them.

They define the actions to be taken for each step. For example, the step **I can see the page is called "Dashboard"** could be implemented as follows in typescript:

```typescript
await expect(page).toHaveTitle('Dashboard');
```

And as part of our stringified assertion, it would look like this:

```typescript
{
  regex: /^I can see the page is called "(.*)"$/,
  argCount: 1,
  generate: ([title]: string[]) => `await expect(page).toHaveTitle('${title}');`
}
```

Note that the `generate` function is a string template that will be used to generate the actual code to be executed. This allows us to write the steps in a more readable way, even though we can't execute them directly. Also, the `regex` is used to match the step definition with the actual step in the feature file, and the `argCount` is used to define how many arguments the step will receive. The `argCount` is used as a validation, or safety net, to ensure that the step is called with the correct number of arguments. If the step is called with a different number of arguments, it will throw an error.

If a new step group is created, it should be added to the [src/steps/index.ts](src/steps/index.ts) file, so that it can be loaded by the Cucumber runner. This is done by importing the step group and adding it to the `allSteps` array.

### Running the Tests

Once you have defined your scenarios and step definitions, you can run your Cucumber tests. To run, execute one of the commands, depending on your need:

For an interface-guided execution, run:

```bash
npm run test:ui
```

For a headless execution, run:

```bash
npm run test
```

You can use the deployed application for the test, or the local environment. Ideally the results would be the same on both cases.

### I don't like the automated generation of feature files

If you prefer to write your own feature files instead of using the automated generation, you can do so by creating a new `.spec.ts` file in the [src/tests](src/tests) folder. The idea behind this is to allow you to write your own tests without the need to use the automated generation process, but the downside is that you will have to write the same instructions again, as the way that the steps are structured, it uses a string template approach to write code during hook phase.

Keep in mind that who benefits from the automated generation is the non-technical team members, as they can write down their scenarios without the need to know how to code. If you have a new set of instructions that you thing will be usefor the team, and more specifically the non-technical team members, please consider adding this set of instructions as a step entry and add it to the list of existing instructions in the [Existing step instructions](#steplist), so that everyone can benefit from it.

## Automated test results

After the tests are executed, it will generate a few artefacts as proof of execution, such as screenshots and videos. This is good for reference, in case of an error, or to validate a scenario with the rest of the team.

When a feature file has a instruction without the corresponding implementation step, it will make the automated test fail. **DON'T PANIC**. This is a expected behavior if you're adding a new instruction. One of the developers will be notified when new things are created so they can deal with the implementation of that specific step.
