package S01_DBAppsIntroduction.exercises;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RemoveVillain {

    private static final String GET_VILLAIN_BY_ID = "SELECT v.name FROM villains AS v WHERE v.id = ?";
    private static final String NO_SUCH_VILLAIN_MESSAGE = "No such villain was found";
    private static final String GET_MINION_COUNT_BY_VILLAINS_ID =
            "SELECT COUNT(mv.minion_id) AS m_count FROM minions_villains AS mv WHERE mv.villain_id = ?";

    private static final String DELETE_MINIONS_VILLAIN_BY_VILLAIN_ID =
            "DELETE FROM minions_villains AS mv WHERE mv.villain_id = ?";
    private static final String DELETE_BY_VILLAIN_ID = "DELETE FROM villains AS v WHERE v.id = ?";

    private static final String COLUMN_LABEL_MINION_COUNT = "m_count";
    private static final String DELETED_VILLAIN_FORMAT = "%s was deleted%n";
    private static final String DELETED_COUNT_OF_MINIONS_FORMAT = "%s minions released";

    public static void main(String[] args) throws SQLException {

        final Connection connection = Utils.getSQLConnection();

        final int villainId = new Scanner(System.in).nextInt();

        final PreparedStatement selectedVillain = connection.prepareStatement(GET_VILLAIN_BY_ID);
        selectedVillain.setInt(1, villainId);
        final ResultSet villainSet = selectedVillain.executeQuery();

        if (!villainSet.next()) {
            System.out.println(NO_SUCH_VILLAIN_MESSAGE);
            return;
        }

        final String villainName = villainSet.getString(Constants.COLUMN_LABEL_NAME);

        final PreparedStatement selectAllMinions = connection.prepareStatement(GET_MINION_COUNT_BY_VILLAINS_ID);
        selectAllMinions.setInt(1, villainId);

        final ResultSet countOfMinionsSet = selectAllMinions.executeQuery();
        countOfMinionsSet.next();
        
        final int countOfDeletedMinions = countOfMinionsSet.getInt(COLUMN_LABEL_MINION_COUNT);

        connection.setAutoCommit(false);

        try (
                PreparedStatement deletedMinionStatement = connection.prepareStatement(DELETE_MINIONS_VILLAIN_BY_VILLAIN_ID);
                PreparedStatement deletedVillainStatement = connection.prepareStatement(DELETE_BY_VILLAIN_ID);
        ) {
            deletedMinionStatement.setInt(1, villainId);
            deletedMinionStatement.executeUpdate();

            deletedVillainStatement.setInt(1, villainId);
            deletedVillainStatement.executeUpdate();

            connection.commit();
            System.out.printf(DELETED_VILLAIN_FORMAT, villainName);
            System.out.printf(DELETED_COUNT_OF_MINIONS_FORMAT, countOfDeletedMinions);
        } catch (SQLException e) {
            e.printStackTrace();

            connection.rollback();
        }

        connection.close();
    }
}
