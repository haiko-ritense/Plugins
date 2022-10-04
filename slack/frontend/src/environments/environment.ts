/*
 * Copyright 2015-2020 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
import {NgxLoggerLevel} from 'ngx-logger';
import {
  UploadProvider,
  ValtimoConfig,
} from '@valtimo/config';
import {authenticationKeycloak} from './auth/keycloak-config.dev';
import {menuConfig} from './menu';

const defaultDefinitionColumns = [
  {
    propertyName: 'createdOn',
    translationKey: 'createdOn',
    sortable: true,
    viewType: 'date',
    default: true
  },
  {
    propertyName: 'modifiedOn',
    translationKey: 'lastModified',
    sortable: true,
    viewType: 'date'
  }
];

export const environment: ValtimoConfig = {
  production: false,
  initializers: [],
  authentication: authenticationKeycloak,
  menu: menuConfig,
  whitelistedDomains: ['localhost:4200'],
  mockApi: {
    endpointUri: '/mock-api/',
  },
  valtimoApi: {
    endpointUri: '/api/',
  },
  swagger: {
    endpointUri: '/v2/api-docs',
  },
  logger: {
    level: NgxLoggerLevel.TRACE,
  },
  definitions: {
    dossiers: [],
  },
  openZaak: {
    catalogus: '00000000-0000-0000-0000-000000000000',
  },
  uploadProvider: UploadProvider.S3,
  caseFileSizeUploadLimitMB: 100,
  defaultDefinitionTable: defaultDefinitionColumns,
  customDefinitionTables: {  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
