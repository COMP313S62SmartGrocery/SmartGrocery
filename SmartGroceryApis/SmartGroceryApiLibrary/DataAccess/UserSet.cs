using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.DataAccess
{
    class UserSet
    {
        public bool AddUser(string username, string password)
        {
            ConnectionManager connection = new ConnectionManager();

            try{
                connection.Open();

                //generating uuid
                string uuid = Guid.NewGuid().ToString();

                SqlCommand cmd = new SqlCommand("INSERT INTO USERS(USERNAME,PASSWORD,AUTH_KEY) values(@username,@password,@authkey)", connection.con);
                cmd.Parameters.Add(new SqlParameter("@username",username));
                cmd.Parameters.Add(new SqlParameter("@password",password));
                cmd.Parameters.Add(new SqlParameter("@authkey", uuid));

                bool ret = cmd.ExecuteNonQuery()>0;

                connection.Close();

                return ret;
            }
            catch(Exception ex){
                try{
                    connection.Close();
                }finally{
                }
            }
            return false;
        }

        public bool RemoveUser(string username,string password)
        {
            ConnectionManager connection = new ConnectionManager();

            try{
                connection.Open();

                SqlCommand cmd = new SqlCommand("DELETE FROM USERS where USERNAME=@username and PASSWORD=@password", connection.con);
                cmd.Parameters.Add(new SqlParameter("@username",username));
                cmd.Parameters.Add(new SqlParameter("@password",password));

                bool ret = cmd.ExecuteNonQuery()>0;

                connection.Close();

                return ret;
            }
            catch(Exception ex){
                try{
                    connection.Close();
                }finally{
                }
            }
            return false;
        }

        public bool UpdatePassword(string username, string password, string newPassword)
        {
            ConnectionManager connection = new ConnectionManager();

            try
            {
                connection.Open();

                SqlCommand cmd = new SqlCommand("UPDATE USERS SET PASSWORD=@newPassword where USERNAME=@username and PASSWORD=@password", connection.con);
                cmd.Parameters.Add(new SqlParameter("@username", username));
                cmd.Parameters.Add(new SqlParameter("@password", password));
                cmd.Parameters.Add(new SqlParameter("@newPassword", newPassword));

                bool ret = cmd.ExecuteNonQuery() > 0;

                connection.Close();

                return ret;
            }
            catch (Exception ex)
            {
                try
                {
                    connection.Close();
                }
                finally
                {
                }
            }
            return false;
        }
        public bool IsExists(string username)
        {
            ConnectionManager connection = new ConnectionManager();

            try
            {
                connection.Open();

                SqlCommand cmd = new SqlCommand("SELECT COUNT(*) FROM USERS where USERNAME=@username", connection.con);
                cmd.Parameters.Add(new SqlParameter("@username", username));

                bool ret = int.Parse(cmd.ExecuteScalar().ToString()) > 0;

                connection.Close();

                return ret;
            }
            catch (Exception ex)
            {
                try
                {
                    connection.Close();
                }
                finally
                {
                }
            }
            return false;
        }

        public string Authenticate(string username, string password)
        {
            ConnectionManager connection = new ConnectionManager();

            try
            {
                connection.Open();

                SqlCommand cmd = new SqlCommand("SELECT AUTH_KEY FROM USERS where USERNAME=@username and PASSWORD=@password", connection.con);
                cmd.Parameters.Add(new SqlParameter("@username", username));
                cmd.Parameters.Add(new SqlParameter("@password", password));

                string ret = cmd.ExecuteScalar().ToString();

                connection.Close();

                return ret;
            }
            catch (Exception ex)
            {
                try
                {
                    connection.Close();
                }
                finally
                {
                }
            }
            return null;
        }

        public string GenerateAuthKey(string username, string password)
        {
            ConnectionManager connection = new ConnectionManager();

            try
            {
                connection.Open();
                string uuid = Guid.NewGuid().ToString();

                SqlCommand cmd = new SqlCommand("UPDATE USERS SET AUTH_KEY=@authkey where USERNAME=@username and PASSWORD=@password", connection.con);
                cmd.Parameters.Add(new SqlParameter("@username", username));
                cmd.Parameters.Add(new SqlParameter("@password", password));
                cmd.Parameters.Add(new SqlParameter("@authkey", uuid));

                string ret = null;

                if (cmd.ExecuteNonQuery() > 0)
                {
                    ret = uuid;
                }

                connection.Close();

                return ret;
            }
            catch (Exception ex)
            {
                try
                {
                    connection.Close();
                }
                finally
                {
                }
            }
            return null;
        }

        public string GenerateSessionKey(string authenticationKey)
        {
            ConnectionManager connection = new ConnectionManager();

            try
            {
                connection.Open();
                string uuid = Guid.NewGuid().ToString();
                DateTime expiry = DateTime.Now.AddHours(2);

                SqlCommand cmd = new SqlCommand("UPDATE USERS SET SESS_KEY=@sesskey and SESS_EXPIRY=@expiry where AUTH_KEY=@authkey", connection.con);
                cmd.Parameters.Add(new SqlParameter("@authkey", authenticationKey));
                cmd.Parameters.Add(new SqlParameter("@sesskey", uuid));
                cmd.Parameters.Add(new SqlParameter("@expiry", expiry.ToString(Constants.DATEFORMAT)));

                string ret = null;

                if (cmd.ExecuteNonQuery() > 0)
                {
                    ret = uuid;
                }

                connection.Close();

                return ret;
            }
            catch (Exception ex)
            {
                try
                {
                    connection.Close();
                }
                finally
                {
                }
            }
            return null;
        }
    }
}
