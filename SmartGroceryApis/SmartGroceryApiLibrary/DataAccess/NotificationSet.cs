using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.DataAccess
{
    public class NotificationSet
    {
        public static bool AddNotification(Notification notification)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("insert into Notifications([From],Subject,Text,Date,Username) "
                                            + "values(@from,@subject,@text,@date,@username)", connection.con);
            cmd.Parameters.Add(new SqlParameter("@from", notification.From));
            cmd.Parameters.Add(new SqlParameter("@subject", notification.Subject));
            cmd.Parameters.Add(new SqlParameter("@text", notification.Text));
            cmd.Parameters.Add(new SqlParameter("@date", DateTime.Now.ToString(Constants.DATEFORMAT)));
            cmd.Parameters.Add(new SqlParameter("@username", notification.Username));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static bool UpdateNotification(string username, long id, string text)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            
            SqlCommand cmd = new SqlCommand("update Notifications set Text=@text where Id=@id AND USERNAME=@username", connection.con);
            cmd.Parameters.Add(new SqlParameter("@text", text));
            cmd.Parameters.Add(new SqlParameter("@id", id));
            cmd.Parameters.Add(new SqlParameter("@username", username));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static bool SetNotificationRead(Notification notification)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();


            SqlCommand cmd = new SqlCommand("update Notifications set IsRead=1 where Id=@id AND USERNAME=@username", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", notification.Id));
            cmd.Parameters.Add(new SqlParameter("@username", notification.Username));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static bool DeleteNotification(Notification notification)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("delete from Notifications where ID=@id AND USERNAME=@username", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", notification.Id));
            cmd.Parameters.Add(new SqlParameter("@username", notification.Username));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static List<Notification> GetNotifications(string username)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List<Notification> notifications = new List<Notification>();

            SqlCommand cmd = new SqlCommand("Select * from Notifications where Username=@username OR Username='ALL' ORDER BY ID DESC", connection.con);
            cmd.Parameters.Add(new SqlParameter("@username", username));
            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                while (sdr.Read())
                {
                    Notification notification = new Notification();
                    notification.Id = long.Parse(sdr["ID"].ToString());
                    notification.From = sdr["FROM"].ToString();
                    notification.Date = sdr["DATE"].ToString();
                    notification.IsRead = bool.Parse(sdr["IsRead"].ToString());
                    notification.Subject = sdr["SUBJECT"].ToString();
                    notification.Text = sdr["TEXT"].ToString();
                    notification.Username = username;

                    notifications.Add(notification);
                }
            }

            sdr.Close();
            connection.Close();

            return notifications;
        }

        public static int CountNotifications(string username)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("Select COUNT(*) from Notifications where Username=@username OR Username='ALL'", connection.con);
            cmd.Parameters.Add(new SqlParameter("@username", username));

            int ret = int.Parse(cmd.ExecuteScalar().ToString());

            connection.Close();

            return ret;
        }
    }
}
