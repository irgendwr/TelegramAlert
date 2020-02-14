import React from 'react';
import PropTypes from 'prop-types';

import CommonNotificationSummary from './CommonNotificationSummary';

class TelegramNotificationSummary extends React.Component {
  static propTypes = {
    type: PropTypes.string.isRequired,
    notification: PropTypes.object,
    definitionNotification: PropTypes.object.isRequired,
  };

  static defaultProps = {
    notification: {},
  };

  render() {
    const { notification } = this.props;
    return (
      <CommonNotificationSummary {...this.props}>
        <React.Fragment>
          {/*<tr>
            <td>Bot Token</td>
            <td>{notification.config.bot_token}</td>
          </tr>*/}
          {/*<tr> // TODO: remove?
            <td>Graylog URL</td>
            <td>{notification.config.graylog_url}</td>
          </tr>*/}
          <tr>
            <td>Recipients (Chat IDs)</td>
            <td>{notification.config.chats}</td>
          </tr>
          <tr>
            <td>Message</td>
            <td>{notification.config.message_template}</td>
          </tr>
          {/*<tr> // TODO: remove?
            <td>Parse Mode</td>
            <td>{notification.config.parse_mode}</td>
          </tr>*/}
          {/*<tr>
            <td>HTTP Proxy Address</td>
            <td>{notification.config.proxy_address}</td>
          </tr>
          <tr>
            <td>Proxy User</td>
            <td>{notification.config.proxy_user}</td>
          </tr>
          <tr>
            <td>Proxy Password</td>
            <td>{notification.config.proxy_password}</td>
          </tr>*/}
        </React.Fragment>
      </CommonNotificationSummary>
    );
  }
}

export default TelegramNotificationSummary;