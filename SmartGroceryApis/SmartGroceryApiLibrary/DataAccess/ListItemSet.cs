using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.DataAccess
{
    public class ListItemSet
    {

        public static List<ListItem> GetListItems(long listId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List<ListItem> items = new List<ListItem>();

            SqlCommand cmd = new SqlCommand("SELECT * FROM LISTITEMS WHERE LISTID=@listid", connection.con);
            cmd.Parameters.Add(new SqlParameter("@listid", listId));

            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                while (sdr.Read())
                {
                    ListItem item = new ListItem();

                    item.Id = long.Parse(sdr["ID"].ToString());
                    item.Name = sdr["NAME"].ToString();
                    item.Quantity = float.Parse(sdr["QUANTITY"].ToString());
                    item.Unit = sdr["UNIT"].ToString();
                    item.LastModified = sdr["LASTMODIFIED"].ToString();
                    item.Reminder = sdr["REMINDER"].ToString();
                    item.ListId = listId;

                    items.Add(item);
                }
            }

            connection.Close();

            return items;
        }
        public static long AddListItem(ListItem listItem)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("insert into ListItems(NAME,QUANTITY,UNIT,LASTMODIFIED,REMINDER,LISTID) "
                                            + "values(@name,@quantity,@unit,@lastmodified,@reminder,@listid)", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", listItem.Name));
            cmd.Parameters.Add(new SqlParameter("@quantity", listItem.Quantity==null ? 0 : listItem.Quantity));
            cmd.Parameters.Add(new SqlParameter("@unit", listItem.Unit == null ? "" : listItem.Unit));
            cmd.Parameters.Add(new SqlParameter("@lastmodified", listItem.LastModified == null ? "" : listItem.LastModified));
            if (string.IsNullOrEmpty(listItem.Reminder)) 
            {
                cmd.Parameters.Add(new SqlParameter("@reminder", ""));
            }
            else
            {
                cmd.Parameters.Add(new SqlParameter("@reminder", DateTime.Parse(listItem.Reminder).ToString(Constants.DATEFORMAT)));
            }
            
            cmd.Parameters.Add(new SqlParameter("@listid", listItem.ListId));

            long ret = -1;

            if (cmd.ExecuteNonQuery() > 0)
            {
                cmd.CommandText = "Select @@Identity";
                ret = long.Parse(cmd.ExecuteScalar().ToString());
            }

            if (listItem.LastModified != null)
            {
                //updating list modified date
                ListSet.ModifyNow(listItem.ListId, listItem.LastModified);
            }

            connection.Close();

            return ret;
        }

        private static long GetListId(long listItemId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("SELECT LISTID FROM LISTITEMS WHERE ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@listid", listItemId));

            SqlDataReader sdr = cmd.ExecuteReader();

            long ret = long.Parse(cmd.ExecuteScalar().ToString());

            connection.Close();

            return ret;
        }

        public static bool UpdateListItem(ListItem listItem)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("UPDATE ListItems SET NAME=@name,QUANTITY=@quantity,UNIT=@unit,"
                                            +"LASTMODIFIED=@lastmodified,REMINDER=@reminder where Id=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", listItem.Name));
            cmd.Parameters.Add(new SqlParameter("@quantity", listItem.Quantity));
            cmd.Parameters.Add(new SqlParameter("@unit", listItem.Unit));
            cmd.Parameters.Add(new SqlParameter("@lastmodified", listItem.LastModified));
            cmd.Parameters.Add(new SqlParameter("@reminder", DateTime.Parse(listItem.Reminder).ToString(Constants.DATEFORMAT)));
            cmd.Parameters.Add(new SqlParameter("@id", listItem.Id));

            bool ret = cmd.ExecuteNonQuery() > 0;

            //updating list modified date
            ListSet.ModifyNow(listItem.ListId, listItem.LastModified);

            connection.Close();

            return ret;
        }

        public static bool DeleteListItem(int itemId, string time)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("delete from LISTITEMS where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", itemId));

            bool ret = cmd.ExecuteNonQuery() > 0;

            //updating list modified date
            ListSet.ModifyNow(GetListId(itemId), time);

            connection.Close();

            return ret;
        }

        public static int CountListItems(string listId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("Select COUNT(*) from LISTITEMS where ListId=@listId", connection.con);
            cmd.Parameters.Add(new SqlParameter("@listId", listId));

            int ret = int.Parse(cmd.ExecuteScalar().ToString());

            connection.Close();

            return ret;
        }
    }
}
