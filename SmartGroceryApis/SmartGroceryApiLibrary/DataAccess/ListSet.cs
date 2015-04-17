using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.DataAccess
{
    public class ListSet
    {
        public static long AddList(List list)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("insert into LISTS(NAME,COLOR,LASTMODIFIED,USERNAME) values(@name,@color,@lastmodified,@username)", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", list.Name));
            cmd.Parameters.Add(new SqlParameter("@color", list.Color));
            cmd.Parameters.Add(new SqlParameter("@lastmodified", list.LastModified));
            cmd.Parameters.Add(new SqlParameter("@username", list.Username));

            long ret = -1;

            if (cmd.ExecuteNonQuery() > 0)
            {
                cmd.CommandText = "Select @@Identity";
                ret = long.Parse(cmd.ExecuteScalar().ToString());
            }

            connection.Close();

            return ret;
        }

        public static string GetLastModified(long listId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("SELECT LASTMODIFIED FROM LISTS where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", listId));

            string ret = cmd.ExecuteScalar().ToString();

            connection.Close();

            return ret;
        }

        public static bool ModifyNow(long listId, string time)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("UPDATE LISTS SET LASTMODIFIED=@lastmodified where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", listId));
            cmd.Parameters.Add(new SqlParameter("@lastmodified", time));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static bool RenameList(long listId, string name)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("UPDATE LISTS SET NAME=@name where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", listId));
            cmd.Parameters.Add(new SqlParameter("@name", name));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static List DuplicateList(long id, string name, string username)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List list = null;

            List originalList = GetList(id);
            if (originalList != null)
            {
                list = new List()
                {
                    Name = name,
                    Color = originalList.Color,
                    LastModified = originalList.LastModified,
                    Username =  username
                };

                //adding list to db
                list.Id = AddList(list);

                //getting list of items in original list
                List<ListItem> items = ListItemSet.GetListItems(originalList.Id);

                //adding items in original list to duplicate list
                foreach (ListItem item in items)
                {
                    //updating id to new lists id
                    item.ListId = list.Id;
                    item.Reminder = null;
                    //adding item to db
                    ListItemSet.AddListItem(item);
                }
            }

            connection.Close();

            return list;
        }


        public static bool ShareList(long listId, string withUsername, User user)
        {
            if (UserSet.IsExists(withUsername))
            {
                ConnectionManager connection = new ConnectionManager();
                connection.Open();

                SqlCommand cmd = new SqlCommand("UPDATE Lists SET USERNAME=USERNAME+@newUser where ID=@id", connection.con);
                cmd.Parameters.Add(new SqlParameter("@id", listId));
                cmd.Parameters.Add(new SqlParameter("@newUser", "," + withUsername));

                bool ret = cmd.ExecuteNonQuery() > 0;

                NotificationSet.AddNotification(new Notification()
                {
                    Username = withUsername,
                    From = user.Username,
                    Text = user.Username+" shared his list with you. You will be able to see list in you lists.\n\nUse share option in Lists to share list with your friends and family!\n\nHave a nice day!",
                    Subject= user.Username+" shared list with you",
                    Date = DateTime.Now.ToString(Constants.DATEFORMAT)
                });

                connection.Close();

                return ret;
            }
            return false;
        }

        private static string GetSharingDetails(long listId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("SELECT USERNAME FROM Lists where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", listId));

            string sharingDetails = cmd.ExecuteScalar().ToString();

            connection.Close();

            return sharingDetails;
        }

        public static bool UnShareList(long listId, string withUsername)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            string sharingDetails = GetSharingDetails(listId);

            sharingDetails = sharingDetails.Replace(","+withUsername,"");

            SqlCommand cmd = new SqlCommand("UPDATE LISTS SET USERNAME=@username WHERE ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", listId));
            cmd.Parameters.Add(new SqlParameter("@username", sharingDetails));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static bool DeleteList(long listId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();
            
            //deleting list items
            SqlCommand cmd = new SqlCommand("delete from ListItems where LISTID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", listId));
            cmd.ExecuteNonQuery();

            cmd.CommandText = "delete from Lists where ID=@id";

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        private static List GetList(long listId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List list = null;

            SqlCommand cmd = new SqlCommand("Select * from Lists where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", listId));
            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                sdr.Read();
                
                    list = new List();
                    list.Id = long.Parse(sdr["Id"].ToString());
                    list.Name = sdr["Name"].ToString();
                    list.Color = sdr["Color"].ToString();
                    list.LastModified = DateTime.Parse(sdr["LASTMODIFIED"].ToString()).ToString(Constants.DATEFORMAT);
                    list.Username = sdr["USERNAME"].ToString();

            }

            sdr.Close();
            connection.Close();

            return list;
        }

        public static List<List> GetLists(string username)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List<List> lists = new List<List>();

            SqlCommand cmd = new SqlCommand("Select * from Lists where Username LIKE '%" + username + "%'", connection.con);
            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                while (sdr.Read())
                {
                    List list = new List();
                    list.Id = long.Parse(sdr["Id"].ToString());
                    list.Name = sdr["Name"].ToString();
                    list.Color = sdr["Color"].ToString();
                    list.LastModified = DateTime.Parse(sdr["LASTMODIFIED"].ToString()).ToString(Constants.DATEFORMAT);
                    list.Username = sdr["Username"].ToString();

                    lists.Add(list);
                }
            }

            sdr.Close();
            connection.Close();

            return lists;
        }

        internal static bool IsListExists(User user, string name)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("Select COUNT(*) from Lists where Name=@name AND Username=@username", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", name));
            cmd.Parameters.Add(new SqlParameter("@username", user.Username));

            int ret = int.Parse(cmd.ExecuteScalar().ToString());

            
            connection.Close();

            return ret>0;
        }
    }
}
