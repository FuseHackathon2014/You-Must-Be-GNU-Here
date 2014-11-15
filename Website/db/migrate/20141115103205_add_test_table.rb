class AddTestTable < ActiveRecord::Migration
  def change
    create_table(:test) do |t|
      ## Database authenticatable
      t.text :json,              null: false, default: ""
     end
  end
end
