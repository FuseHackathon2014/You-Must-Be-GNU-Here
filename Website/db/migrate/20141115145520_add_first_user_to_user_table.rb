class AddFirstUserToUserTable < ActiveRecord::Migration
  def change
    user = User.create(id:1, :email => 'Justin Gregorio', :password => 'azazel243')
  end
end
