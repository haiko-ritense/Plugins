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

import {NgxLoggerLevel} from 'ngx-logger';
import {
  ROLE_ADMIN,
  ROLE_DEVELOPER,
  ROLE_USER,
  UploadProvider,
  ValtimoConfig,
} from '@valtimo/config';
import {openZaakExtensionInitializer} from '@valtimo/open-zaak';
import {authenticationKeycloak} from './auth/keycloak-config.test';
import { menuConfig } from './menu';

const defaultDefinitionColumns = [
  {
    propertyName: 'sequence',
    translationKey: 'referenceNumber',
    sortable: true,
  },
  {
    propertyName: 'createdBy',
    translationKey: 'createdBy',
    sortable: true,
  },
  {
    propertyName: 'createdOn',
    translationKey: 'createdOn',
    sortable: true,
    viewType: 'date',
    default: true,
  },
  {
    propertyName: 'modifiedOn',
    translationKey: 'lastModified',
    sortable: true,
    viewType: 'date',
  },
];

export const environment: ValtimoConfig = {
  production: true,
  initializers: [openZaakExtensionInitializer],
  authentication: authenticationKeycloak,
  menu: menuConfig,
  whitelistedDomains: [],
  swagger: {
    endpointUri: '/v2/api-docs',
  },
  mockApi: {
    endpointUri: '/mock-api/',
  },
  valtimoApi: {
    endpointUri: '/api/',
  },
  logger: {
    level: NgxLoggerLevel.ERROR,
  },
  definitions: {
    dossiers: [],
  },
  openZaak: {
    catalogus: '8225508a-6840-413e-acc9-6422af120db1',
  },
  uploadProvider: UploadProvider.OPEN_ZAAK,
  caseFileSizeUploadLimitMB: 10,
  defaultDefinitionTable: defaultDefinitionColumns,
  customDefinitionTables: {},
  featureToggles: {
    showUserNameInTopBar: true,
  },
};
