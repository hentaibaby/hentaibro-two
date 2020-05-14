package hentaibro.database;

public class DAOFactory
{
    private static HentaiDAO hentaiDAO;
    private static UserDAO userDAO;

    public static HentaiDAO getHentaiDao()
    {
        if (hentaiDAO == null)
        {
            hentaiDAO = new HentaiDAO();
        }
        return hentaiDAO;
    }

    public static UserDAO getUserDAO()
    {
        if (userDAO == null)
        {
            userDAO = new UserDAO();
        }
        return userDAO;
    }
}
