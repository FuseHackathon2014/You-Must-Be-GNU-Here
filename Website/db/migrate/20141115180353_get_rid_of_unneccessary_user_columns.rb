class GetRidOfUnneccessaryUserColumns < ActiveRecord::Migration
  def change
    remove_columns :users, :priority, :message, :special_instructions, :location
  end
end
