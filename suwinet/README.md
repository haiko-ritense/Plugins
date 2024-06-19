# Slack plugin

For reading information from Suwinet (https://www.bkwi.nl/producten/suwinet-services/).

## Capabilities

This suwinet plugin can access Suwinet soap webservices, and process the response.

## Example application

This project contains a working example application which is meant to showcase the slack plugin. 
It will send a request to a soap webservice to retrieve BRP data, which is one of the existing data sources of Siwunet.
Other resources are Kadaster, DUO, RWD, UWV, SVB... 

### Running the example application

#### Start docker

Make sure docker is running. Then use the following commands:

```shell
cd slack
docker compose up
```

#### Start frontend

```shell
cd slack/frontend
npm install
npm run start
```

#### Start backend

By gradle script:

`Plugins -> slack -> backend -> app -> Tasks -> application -> bootRun`

Or use commend line:

```shell
brew install gradle

cd slack/backend/app
gralde bootRun
```

#### Keycloak users

The example application has a few test users that are preconfigured.

| Name | Role | Username | Password |
|---|---|---|---|
| James Vance | ROLE_USER | user | user |
| Asha Miller | ROLE_ADMIN | admin | admin |
| Morgan Finch | ROLE_DEVELOPER | developer | developer |

## Source code

The source code is split up into 2 modules:

1. [Frontend](./frontend)
2. [Backend](./backend)
