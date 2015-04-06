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
        private ListSet listSet;

        public ListItemSet()
        {
            listSet = new ListSet();
        }

        public bool AddListItem(ListItem listItem)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("insert into ListItems(NAME,QUANTITY,UNIT,LASTMODIFIED,REMINDER,LISTID) "
                                            + "values(@name,@quantity,@unit,@lastmodified,@reminder,@listid)", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", listItem.Name));
            cmd.Parameters.Add(new SqlParameter("@quantity", listItem.Quantity));
            cmd.Parameters.Add(new SqlParameter("@unit", listItem.Unit));
            cmd.Parameters.Add(new SqlParameter("@lastmodified", DateTime.Now.ToString(Constants.DATEFORMAT)));
            cmd.Parameters.Add(new SqlParameter("@reminder", DateTime.Parse(listItem.Reminder).ToString(Constants.DATEFORMAT)));
            cmd.Parameters.Add(new SqlParameter("@listid", listItem.ListId));

            bool ret = cmd.ExecuteNonQuery() > 0;

            //updating list modified date
            listSet.ModifyNow(listItem.ListId);

            connection.Close();

            return ret;
        }

        public List<ListItem> GetListItems(long listId)
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

        private long GetListId(long listItemId)
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

        public bool UpdateListItem(ListItem listItem)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("UPDATE ListItems SET NAME=@name,QUANTITY=@quantity,UNIT=@unit,"
                                            +"LASTMODIFIED=@lastmodified,REMINDER=@reminder where Id=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", listItem.Name));
            cmd.Parameters.Add(new SqlParameter("@quantity", listItem.Quantity));
            cmd.Parameters.Add(new SqlParameter("@unit", listItem.Unit));
            cmd.Parameters.Add(new SqlParameter("@lastmodified", DateTime.Now.ToString(Constants.DATEFORMAT)));
            cmd.Parameters.Add(new SqlParameter("@reminder", DateTime.Parse(listItem.Reminder).ToString(Constants.DATEFORMAT)));
            cmd.Parameters.Add(new SqlParameter("@id", listItem.Id));

            bool ret = cmd.ExecuteNonQuery() > 0;

            //updating list modified date
            listSet.ModifyNow(listItem.ListId);

            connection.Close();

            return ret;
        }

        public bool DeleteListItem(int itemId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("delete from LISTITEMS where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", itemId));

            bool ret = cmd.ExecuteNonQuery() > 0;

            //updating list modified date
            listSet.ModifyNow(GetListId(itemId));

            connection.Close();

            return ret;
        }

        public int CountListItems(string listId)
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
