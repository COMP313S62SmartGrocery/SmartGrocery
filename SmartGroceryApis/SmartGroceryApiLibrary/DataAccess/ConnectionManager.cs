using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.Sql;
using System.Data.SqlClient;
using System.Configuration;

namespace SmartGroceryApiLibrary.DataAccess
{
    class ConnectionManager
    {
        public SqlConnection con;

        public ConnectionManager()
        {
            con = new SqlConnection(ConfigurationManager.ConnectionStrings["SmartGroceryDb"].ConnectionString);
        }

        public void Open()
        {
            if (con.State != System.Data.ConnectionState.Open)
                con.Open();
        }

        public void Close()
        {
            if (con.State == System.Data.ConnectionState.Open)
                con.Close();
        }
    }
}
