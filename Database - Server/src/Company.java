public class Company {
    private String company_name_id;
    private String company_name;
    private String url;
    private Integer year_founded;
    private String city;
    private String state;
    private Integer zip_code;
    private String company_type;
    private String company_category;

    public Company(String company_name_id,String company_name,  String url, Integer year_founded, String city, String state, Integer zip_code, String company_type, String company_category) {
        this.company_name = company_name;
        this.url = url;
        this.year_founded = year_founded;
        this.city = city;
        this.company_name_id = company_name_id;
        this.state = state;
        this.zip_code = zip_code;
        this.company_type = company_type;
        this.company_category = company_category;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getYear_founded() {
        return year_founded;
    }

    public void setYear_founded(Integer year_founded) {
        this.year_founded = year_founded;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany_name_id() {
        return company_name_id;
    }

    public void setCompany_name_id(String company_name_id) {
        this.company_name_id = company_name_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getZip_code() {
        return zip_code;
    }

    public void setZip_code(Integer zip_code) {
        this.zip_code = zip_code;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getCompany_category() {
        return company_category;
    }

    public void setCompany_category(String company_category) {
        this.company_category = company_category;
    }

    @Override
    public String toString() {
        // return formatted string
        return String.format("%-40s %-50s %-4s %-20s %-5s %-5s %-20s %-30s\n",  company_name, url, year_founded, city, state, zip_code, company_type, company_category);
    }
}
