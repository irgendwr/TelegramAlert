import React from 'react';
import PropTypes from 'prop-types';
import { ControlLabel, FormGroup, HelpBlock } from 'react-bootstrap';
import lodash from 'lodash';

import MultiSelect from './components/MultiSelect';
import Input from './components/Input';
import FormsUtils from './utils/FormUtils';

const DEFAULT_MESSAGE_TEMPLATE = `<b>\${event.message}</b>\${if event.timerange_start}
Timerange: \${event.timerange_start} to \${event.timerange_end}\${end}\${if streams}
Streams:\${foreach streams stream} <a href='\${stream.url}'>\${stream.title}</a>\${end}\${end}
\${if message_too_long}
An alert was triggered, but the message was too long. Please check the Graylog interface for details.
\${else}\${if backlog}<code>\${foreach backlog message}
\${message.message}
\${end}</code>\${else}
<i>- no backlog -</i>
\${end}
\${end}`;

class TelegramNotificationForm extends React.Component {
  static propTypes = {
    config: PropTypes.object.isRequired,
    validation: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired,
  };

  static defaultConfig = {
    bot_token: '',
    graylog_url: (document && document.location ? document.location.origin : ''),
    chats: [],
    message_template: DEFAULT_MESSAGE_TEMPLATE,
    proxy_address: '',
    proxy_user: '',
    proxy_password: '',
  };

  propagateChange = (key, value) => {
    const { config, onChange } = this.props;
    const nextConfig = lodash.cloneDeep(config);
    nextConfig[key] = value;
    onChange(nextConfig);
  };

  handleChange = event => {
    const { name } = event.target;
    this.propagateChange(name, FormsUtils.getValueFromInput(event.target));
  };

  handleChatsChange = selectedOptions => {
    this.propagateChange('chats', selectedOptions === '' ? [] : selectedOptions.split(','));
  };

  render() {
    const { config, validation } = this.props;

    return (
      <React.Fragment>
        <Input id="notification-bot-token"
               name="bot_token"
               label="Bot Token"
               type="password"
               bsStyle={validation.errors.bot_token ? 'error' : null}
               help={lodash.get(validation, 'errors.bot_token[0]', <>Bot Token from <a href="https://t.me/BotFather" target="_blank" rel="noopener">@BotFather</a></>)}
               value={config.bot_token || ''}
               onChange={this.handleChange}
               required />

        <Input id="notification-graylogURL"
               name="graylog_url"
               label="Graylog URL"
               type="text"
               bsStyle={validation.errors.graylog_url ? 'error' : null}
               help={lodash.get(validation, 'errors.graylog_url[0]', 'URL to your Graylog web interface. Used to build links in alarm notification.')}
               value={config.graylog_url || ''}
               onChange={this.handleChange}
               required />

        <FormGroup controlId="notification-chats"
                   validationState={validation.errors.chats ? 'error' : null}>
          <ControlLabel>Recipients (Chat IDs)</ControlLabel>
          <MultiSelect id="notification-chats"
                      value={Array.isArray(config.chats) ? config.chats.join(',') : ''}
                      addLabelText={'Add chat ID "{label}"?'}
                      options={[]}
                      onChange={this.handleChatsChange}
                      allowCreate />
          <HelpBlock>
           {lodash.get(validation, 'errors.chats[0]', <>Telegram chat IDs of the recipients. See <a href="https://irgendwr.github.io/TelegramAlert/message-tool" target="_blank" rel="noopener">message-tool</a>.</>)}
          </HelpBlock>
        </FormGroup>
        
        <Input id="notification-message-template"
               name="message_template"
               label="Message Template"
               type="textarea"
               rows={10}
               bsStyle={validation.errors.message_template ? 'error' : null}
               help={lodash.get(validation, 'errors.message_template[0]', <>This uses the same syntax as the EmailNotification Template. See <a href="https://go2docs.graylog.org/5-0/interacting_with_your_log_data/notifications.html?tocpath=Interacting%20with%20Your%20Log%20Data%7CAlerts%20and%20Notifications%7CNotifications%7C_____0#EmailAlertNotification" target="_blank" rel="noopener">Graylog documentation</a> for more details. Telegram messages are <b>limited to 4096 characters</b>.</>)}
               value={config.message_template || ''}
               onChange={this.handleChange}
               required />
        
        <Input id="notification-proxy-address"
               name="proxy_address"
               label={<>HTTP Proxy Address <small className="text-muted">(Optional)</small></>}
               type="text"
               bsStyle={validation.errors.proxy_address ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_address[0]', 'HTTP Proxy Address in the following format: <ProxyAddress>:<Port>')}
               value={config.proxy_address || ''}
               onChange={this.handleChange} />
        { config.proxy_address ? <>
        <Input id="notification-proxy-user"
               name="proxy_user"
               label={<>HTTP Proxy User <small className="text-muted">(Optional)</small></>}
               type="text"
               bsStyle={validation.errors.proxy_user ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_user[0]', '')}
               value={config.proxy_user || ''}
               onChange={this.handleChange} />
        <Input id="notification-proxy-password"
               name="proxy_password"
               label={<>HTTP Proxy Password <small className="text-muted">(Optional)</small></>}
               type="password"
               bsStyle={validation.errors.proxy_password ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_password[0]', '')}
               value={config.proxy_password || ''}
               onChange={this.handleChange} />
        </> : null }
      </React.Fragment>
    );
  }
}

export default TelegramNotificationForm;