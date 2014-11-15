class RequestController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  require 'json'

  def handle_request
    require 'rubygems' # not necessary with ruby 1.9 but included for completeness
    require 'twilio-ruby'

    # put your own credentials here
    account_sid = 'AC846faf75d0ec47c06eb9438ddd3b2542'
    auth_token = '6179b04cc94f6e1e2151871170b2b2bd'

    # set up a client to talk to the Twilio REST API
    @client = Twilio::REST::Client.new account_sid, auth_token

    # alternatively, you can preconfigure the client like so
    Twilio.configure do |config|
      config.account_sid = account_sid
      config.auth_token = auth_token
    end

    # and then you can create a new client without parameters
    @client = Twilio::REST::Client.new

    @client.messages.create(
      from: '+12138175173',
      to: '+14252206080',
      body: "{\"e\":\"#{params[:emergency]}\",\"sev\":\"#{params[:severity]}\",\"lon\":
\"#{params[:longitude]}\",\"lat\":\"#{params[:latitude]}\",\"sp\":\"#{params[:special_instructions]}\",\"loc\":\"#{params[:location]}\"}"
    )
    redirect_to "/layouts/application"
  end

  def request_params
    params.require(:user).permit(:message)
  end

end
