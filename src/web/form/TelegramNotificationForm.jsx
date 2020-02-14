import React from 'react';
import PropTypes from 'prop-types';
import { ControlLabel, FormGroup, HelpBlock } from 'react-bootstrap';
import lodash from 'lodash';

import { MultiSelect, SourceCodeEditor } from 'components/common';
import { Input } from 'components/bootstrap';
import FormsUtils from 'util/FormsUtils';

const DEFAULT_MESSAGE_TEMPLATE = ''
     + '<a href="${stream_url}">${stream.title}</a>: ${alert_condition.title}\n'
     + '<code>${foreach backlog message}\n'
     + '${message.message}\n'
     + '${end}</code>';

class TelegramNotificationForm extends React.Component {
  static propTypes = {
    config: PropTypes.object.isRequired,
    validation: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired,
  };

  static defaultConfig = {
    bot_token: '',
    //graylog_url: '', // TODO: remove?
    chats: [],
    message_template: DEFAULT_MESSAGE_TEMPLATE,
    //parse_mode: '', // TODO: remove?
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

  handleChange = (event) => {
    const { name } = event.target;
    this.propagateChange(name, FormsUtils.getValueFromInput(event.target));
  };

  handleMessageTemplateChange = (nextValue) => {
    this.propagateChange('message_template', nextValue);
  };

  handleChatsChange = (nextValue) => {
    this.propagateChange('chats', nextValue === '' ? [] : nextValue.split(','));
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
               help={lodash.get(validation, 'errors.bot_token[0]', 'HTTP API Token from @BotFather')}
               value={config.bot_token || ''}
               onChange={this.handleChange}
               required />

        {/*<Input id="notification-graylogURL" // TODO: remove?
               name="graylog_url"
               label="Graylog URL"
               type="text"
               bsStyle={validation.errors.graylog_url ? 'error' : null}
               help={lodash.get(validation, 'errors.graylog_url[0]', 'URL to your Graylog web interface. Used to build links in alarm notification.')}
               value={config.graylog_url || ''}
               onChange={this.handleChange}
               required />*/}

        <FormGroup controlId="notification-chats"
                   validationState={validation.errors.chats ? 'error' : null}>
          <ControlLabel>Recipients (Chat IDs)</ControlLabel>
          <MultiSelect id="notification-chats"
                      value={Array.isArray(config.chats) ? config.chats.join(',') : ''}
                      addLabelText={'Add chat ID "{label}"?'}
                      placeholder="Enter chat ID"
                      options={[]}
                      onChange={this.handleChatsChange}
                      allowCreate />
          <HelpBlock>
           {lodash.get(validation, 'errors.chats[0]', 'Telegram chat IDs of the recipients. See https://irgendwr.github.io/TelegramAlert/message-tool')}
          </HelpBlock>
        </FormGroup>

        <FormGroup controlId="notification-message-template"
                   validationState={validation.errors.message_template ? 'error' : null}>
          <ControlLabel>Message Template</ControlLabel>
          <SourceCodeEditor id="notification-message-template"
                            mode="text"
                            theme="light"
                            value={config.message_template || ''}
                            onChange={this.handleMessageTemplateChange} />
          <HelpBlock>
            {lodash.get(validation, 'errors.message_template[0]', 'See http://docs.graylog.org/en/latest/pages/streams/alerts.html#email-alert-notification for more details.')}
          </HelpBlock>
        </FormGroup>

        <Input id="notification-proxy-address"
               name="proxy_address"
               label="HTTP Proxy Address (optional)"
               type="text"
               bsStyle={validation.errors.proxy_address ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_address[0]', 'HTTP Proxy Address in the following format: <ProxyAddress>:<Port>')}
               value={config.proxy_address || ''}
               onChange={this.handleChange} />
        <Input id="notification-proxy-user"
               name="proxy_user"
               label="HTTP Proxy User (optional)"
               type="text"
               bsStyle={validation.errors.proxy_user ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_user[0]', '')}
               value={config.proxy_user || ''}
               onChange={this.handleChange} />
        <Input id="notification-proxy-password"
               name="proxy_password"
               label="HTTP Proxy Password (optional)"
               type="password"
               bsStyle={validation.errors.proxy_password ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_password[0]', '')}
               value={config.proxy_password || ''}
               onChange={this.handleChange} />
      </React.Fragment>
    );
  }
}

export default TelegramNotificationForm;