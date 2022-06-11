import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Connection con;
    private static String DATABASE_NAME = "postgres";
    private static String USERNAME = "postgres";
    private static String PASSOWRD = "admin123";


    private static void connectDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE_NAME, USERNAME, PASSOWRD);
    }

    public static String getAllCategories() {
        StringBuilder sb = new StringBuilder();
        try {
            connectDatabase();
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT company_category FROM company");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("company_category"));
                if(rs.next())
                    sb.append(",");
            }
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getCompaniesByCategory(String catagory) {
        StringBuilder sb = new StringBuilder();
        try {
            connectDatabase();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM company WHERE company_category = ?");
            ps.setString(1, catagory);
            ArrayList<Company> companies = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Company company = new Company(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8), rs.getString(9));
                companies.add(company);
            }
            sb.append(String.format("%-40s %-50s %-4s %-20s %-5s %-5s %-20s %-30s\n", "Company Name", "URL", "Year", "City", "State", "Zip", "Company Type", "Company Category"));
            sb.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

            for (Company company : companies) {
                sb.append(company.toString());
            }

            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String searchCompaniesByName(String pattern) {
        StringBuilder sb = new StringBuilder();
        try {
            connectDatabase();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM company WHERE company_name LIKE ? ORDER BY company_category LIMIT 100");
            ps.setString(1,  pattern + "%");
            ArrayList<Company> companies = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Company company = new Company(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8), rs.getString(9));
                companies.add(company);
            }
            sb.append(String.format("%-40s %-50s %-4s %-20s %-5s %-5s %-20s %-30s\n", "Company Name", "URL", "Year", "City", "State", "Zip", "Company Type", "Company Category"));
            sb.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            for (Company company : companies) {
                sb.append(company);
            }
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}