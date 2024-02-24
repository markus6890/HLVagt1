package com.gmail.markushygedombrowski.settings.deliveredItems;

import com.gmail.markushygedombrowski.settings.Sql;

import java.sql.*;
import java.util.UUID;

public class DeliveredItemsLoader {

    private final Sql sql;

    public DeliveredItemsLoader(Sql sql) {
        this.sql = sql;
    }

    public DeliveredItems loadDeliveredItems(UUID uuid) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = sql.getConnection();
            statement = connection.prepareStatement("SELECT * FROM DeliveredItems WHERE uuid = ?");
            sql.createDeliveredItemsTableIfNotExists();
            statement.setString(1, uuid.toString());


            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createDeliveredItemsFromResultSet(resultSet);
            } else {
                return createNewDeliveredItems(connection, uuid);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.closeAllSQL(connection, statement, resultSet);
        }
        return null;
    }

    private DeliveredItems createDeliveredItemsFromResultSet(ResultSet resultSet) throws SQLException {
        return new PLayerDeliveredItems(
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getInt("seed"),
                resultSet.getInt("bread"),
                resultSet.getInt("ironHelmet"),
                resultSet.getInt("ironChestplate"),
                resultSet.getInt("ironLeggings"),
                resultSet.getInt("ironBoots"),
                resultSet.getInt("ironSword"),
                resultSet.getInt("diamondHelmet"),
                resultSet.getInt("diamondChestplate"),
                resultSet.getInt("diamondLeggings"),
                resultSet.getInt("diamondBoots"),
                resultSet.getInt("diamondSword"),
                resultSet.getInt("heads")
        );
    }

    private DeliveredItems createNewDeliveredItems(Connection connection, UUID uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO DeliveredItems (uuid, seed, bread, ironHelmet, ironChestplate, ironLeggings, ironBoots, ironSword, diamondHelmet, diamondChestplate, diamondLeggings, diamondBoots, diamondSword, heads) VALUES (?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        }
        return new PLayerDeliveredItems(uuid, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public void saveDeliveredItems(DeliveredItems deliveredItems) {
        String updateQuery = "UPDATE DeliveredItems SET seed = ?, bread = ?, ironHelmet = ?, ironChestplate = ?, ironLeggings = ?, ironBoots = ?, ironSword = ?, diamondHelmet = ?, diamondChestplate = ?, diamondLeggings = ?, diamondBoots = ?, diamondSword = ?, heads = ? WHERE uuid = ?";

        try (Connection connection = sql.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setInt(1, deliveredItems.getSeed());
            statement.setInt(2, deliveredItems.getBread());
            statement.setInt(3, deliveredItems.getIronHelmet());
            statement.setInt(4, deliveredItems.getIronChestplate());
            statement.setInt(5, deliveredItems.getIronLeggings());
            statement.setInt(6, deliveredItems.getIronBoots());
            statement.setInt(7, deliveredItems.getIronSword());
            statement.setInt(8, deliveredItems.getDiamondHelmet());
            statement.setInt(9, deliveredItems.getDiamondChestplate());
            statement.setInt(10, deliveredItems.getDiamondLeggings());
            statement.setInt(11, deliveredItems.getDiamondBoots());
            statement.setInt(12, deliveredItems.getDiamondSword());
            statement.setInt(13, deliveredItems.getHeads());
            statement.setString(14, deliveredItems.getUUID().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}