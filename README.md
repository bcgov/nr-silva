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
been used in this app. And also everything you need to get started, build,
test and deploy.

- React Progressive Web Application
  - TypeScript
  - Context API
  - React Testing Library
  - Jest
- Lint
  - Airbnb ESLint
- Tools
  - Docker
  - Microsoft Visual Studio Code
- Styling
  - Carbon Design System
  - Bootstrap
- Authentication
  - AWS Cognito (FAM)

# Getting started

Once you have cloned this repository, you can get the app running by typing
`npm install` and then `npm run start` from the project root directory. Then
head to http://localhost:3000.

Be aware of the required environment variables:

- REACT_APP_USER_POOLS_ID
- REACT_APP_USER_POOLS_WEB_CLIENT_ID

To run the unit tests all you need is `npm run test`.

And with Docker:

```bash
docker compose --env-file ./frontend/.env.local up -d
```

Before writing your first line of code, please take a moment and check out
our [CONTRIBUTING](CONTRIBUTING.md) guide.

## Getting help

As mentioned, we're here to help. Feel free to start a conversation
on Rocket chat, you can search for `@jazz.grewal`.
