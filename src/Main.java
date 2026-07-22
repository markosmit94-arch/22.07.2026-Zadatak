import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

        DataSource dataSource = createDataSource();
        try(Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

            System.out.println("Uspjesno ste spojeni na bazu!");
            boolean izbor=true;
            while(izbor){
                System.out.println("1 Nova drzava");
                System.out.println("2 Izmjena postojece drzave");
                System.out.println("3 Brisanje postojece drzave");
                System.out.println("4 Prikaz svih drzava sortiranih po nazivu");
                System.out.println("5 Kraj");
                String opcija = reader.readLine();

                switch(opcija){
                    case "1":
                        System.out.println("\nUnesite naziv drzave");
                        String drzava = reader.readLine();
                        String sql=String.format("INSERT INTO Drzava(Naziv) VALUES ('%s')", drzava);
                        stmt.executeUpdate(sql);
                        System.out.println("Unos uspjesan!");
                        break;

                    case "2":
                        System.out.println("\nPopis drzava");
                        ResultSet rs = stmt.executeQuery("SELECT IDDrzava, Naziv FROM Drzava");
                        while(rs.next()){
                            System.out.printf("%d %s\n", rs.getInt("IDDrzava"), rs.getString("Naziv"));
                        }
                        rs.close();

                        System.out.println("\nUnesite ID drzave za izmjenu");
                        int id=Integer.parseInt(reader.readLine());
                        System.out.println("\nUnesite naziv drzave");
                        String naziv = reader.readLine();
                        String sql2=String.format("UPDATE Drzava SET Naziv='%s' WHERE IDDrzava='%d'", naziv, id);
                        stmt.executeUpdate(sql2);
                        System.out.println("Izmjena uspjesna!");
                        break;

                    case "3":
                        System.out.println("\nPopis drzava");
                        ResultSet rs3 = stmt.executeQuery("SELECT IDDrzava, Naziv FROM Drzava");
                        while(rs3.next()){
                            System.out.printf("%d %s\n", rs3.getInt("IDDrzava"), rs3.getString("Naziv"));
                        }
                        rs3.close();

                        System.out.println("\nUnesite ID drzave za brisanje");
                        int id2 = Integer.parseInt(reader.readLine());
                        String sql3="DELETE FROM Drzava WHERE IDDrzava = '"+id2+"'";
                        stmt.executeUpdate(sql3);
                        System.out.println("Brisanje uspjesno!");
                        break;

                    case "4":
                        System.out.println("\nPopis drzava sortiranih po nazivu:");
                        ResultSet rs2 = stmt.executeQuery("SELECT IDDrzava, Naziv FROM Drzava ORDER BY Naziv");
                        while (rs2.next()) {
                            System.out.printf("%d %s\n", rs2.getInt("IDDrzava"), rs2.getString("Naziv"));
                        }
                        rs2.close();
                        break;

                    case "5":
                        izbor=false;
                        break;
                }
            }

        }

        catch (SQLException e){
            System.err.println("Greska prilikom spajanja na bazu!");
            e.printStackTrace();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);
        return ds;
    }
}
