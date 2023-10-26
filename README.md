[![Lifecycle:Experimental](https://img.shields.io/badge/Lifecycle-Experimental-339999)](https://github.com/bcgov/nr-spar-webapp)


# Natural Resources SILVA Front-End Starting Web Application

This repository holds a set of policies, standard, guides and pipelines to get
started with a React TS Web Application.

## Our Policy

- Work in the open: That means that everything we do should be open, should be
public. Please, don't create private repositories unless you have a very strong
reason. Keeping things public is a must follow rule for BC Government.
- Customer centred services: All the work that's been created is to improve
users, customers, and friends usability and experience. Is important to keep
that in mind, because as engineers sometimes we face technical issues, however, our goal is to have a good product.
- Community based work: Remember that you're not alone. It's very likely that
your problem is someone else's problem. Let's figure it out together. So, ask
a question using our channels. We have [our own Stackoverflow](https://stackoverflow.developer.gov.bc.ca/)
and [our Rocket Chat](https://chat.developer.gov.bc.ca/) channel.

# Stack

Here you will find a comprehensive list of all the languages and tools that are
been used on SILVA. And also everything you need to get started, build,
test and deploy.

## Front end
- Progressive Web Application
  - [React](https://react.dev/)
  - [React Redux](https://react-redux.js.org/)
  - [TypeScript](https://www.typescriptlang.org/)
  - [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/)
  - [Vitest](https://vitest.dev/)
  - [Vite](https://vitejs.dev/)
  - [Node 20](https://nodejs.org/download/release/v20.7.0/)
- Lint
  - [Standard TS ESLint](https://github.com/standard/eslint-config-standard-with-typescript)
- Tools
  - Docker & Docker compose
  - Microsoft VS Code
- Styling
  - [Carbon Design System](https://github.com/bcgov/nr-theme)
  - [Bootstrap CSS](https://getbootstrap.com/)
- Authentication & Authorization
  - [FAM (AWS Cognito)](https://github.com/bcgov/nr-forests-access-management)

## Back end
- REST API
  - [Java 17 with GraalVM](https://www.graalvm.org/)
  - [Spring Boot Web](https://spring.io/guides/gs/spring-boot/)
  - [Apache Maven](https://maven.apache.org/)
  - [Hibernate ORM](https://hibernate.org/orm/)
  - [Oracle JDBC](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html)
- Lint
  - [Google Java Checkstyle ](https://checkstyle.org/styleguides/google-java-style-20180523/javaguide.html)
- IDE
  - [VSCode](https://code.visualstudio.com/) can be a great choice
- Authentication & Authorization
  - OAuth 2.0 with JWT Token-based requests.

# Getting started

Once you have cloned this repository, you can get the app running by typing
at the project root directory:

```sh
docker compose up -d
```

Then head to http://localhost:3000 too see SILVA running. You can see the REST API working at http://localhost:8080

Before writing your first line of code, please take a moment and check out
our [CONTRIBUTING](CONTRIBUTING.md) guide.

## Getting help

As mentioned, we're here to help. Feel free to reach out and 
start a conversation on Rocket chat, you can search for
`@jazz.grewal` or `@ricardo.campos`.
