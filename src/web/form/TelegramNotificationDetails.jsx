/*
 * Copyright (C) 2020 Graylog, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
import React from 'react';
import PropTypes from 'prop-types';

import ReadOnlyFormGroup from './components/ReadOnlyFormGroup';

const TelegramNotificationDetails = ({ notification }) => {
  return (
    <>
      <ReadOnlyFormGroup label="Graylog URL" value={notification.config.graylog_url} />
      <ReadOnlyFormGroup label="Recipients (Chat IDs)" value={notification.config.chats} />
      <ReadOnlyFormGroup label="Message" value={notification.config.message_template} />
      { notification.config.proxy_address ?
        <ReadOnlyFormGroup label="HTTP Proxy Address" value={notification.config.proxy_address} />
      : null}
      { notification.config.proxy_user ?
        <ReadOnlyFormGroup label="Proxy User" value={notification.config.proxy_user} />
      : null}
      { notification.config.proxy_password ?
        <ReadOnlyFormGroup label="Proxy Password" value={<small className="text-muted">(hidden)</small>} />
      : null}
    </>
  );
};

TelegramNotificationDetails.propTypes = {
  notification: PropTypes.object.isRequired,
};

export default TelegramNotificationDetails;