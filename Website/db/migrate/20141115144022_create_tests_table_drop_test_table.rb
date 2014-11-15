class CreateTestsTableDropTestTable < ActiveRecord::Migration
  def change
    drop_table :test
    add_column :users, :message, :text, :limit => 255, :default => '', null: false
    add_column :users, :priority, :integer, default: nil, null: false
    add_column :users, :phone_number, :integer, default: 0
    add_column :users, :special_instructions, :text, default:  ''
    add_column :users, :location, :string, default: ''
  end
end
