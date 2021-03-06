﻿using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.DataAccess
{
    public class ItemHistorySet
    {
        public static bool AddItemHistory(ItemHistory itemHistory)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("insert into ItemHistory(NAME,QUANTITY,UNIT,DATE,USERNAME) "
                                            + "values(@name,@quantity,@unit,@date,@username)", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", itemHistory.Name));
            cmd.Parameters.Add(new SqlParameter("@quantity", itemHistory.Quantity));
            cmd.Parameters.Add(new SqlParameter("@unit", itemHistory.Unit));
            cmd.Parameters.Add(new SqlParameter("@date", itemHistory.Date));
            cmd.Parameters.Add(new SqlParameter("@username", itemHistory.Username));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static List<ItemHistory> GetItemHistory(string itemName, string username,string month, string year)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List<ItemHistory> history = new List<ItemHistory>();

            SqlCommand cmd;
            if (string.IsNullOrEmpty(month) || string.IsNullOrEmpty(year))
            {
                cmd =new SqlCommand("SELECT * FROM ITEMHISTORY WHERE NAME=@name AND USERNAME=@username ORDER BY ID", connection.con);
            }
            else
            {
                cmd = new SqlCommand("SELECT Id,Name,Quantity,Unit,FORMAT(CONVERT(DATE,DATE,103),'dd-MM-yyyy') as [Date] FROM ItemHistory WHERE NAME=@name AND USERNAME=@username AND MONTH(CONVERT(DATE,[Date],103))=@month AND YEAR(CONVERT(DATE,[DATE],103))=@year", connection.con);
                cmd.Parameters.Add(new SqlParameter("@month", month));
                cmd.Parameters.Add(new SqlParameter("@year", year));
            }
            cmd.Parameters.Add(new SqlParameter("@name", itemName));
            cmd.Parameters.Add(new SqlParameter("@username", username));

            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                while(sdr.Read())
                {
                    ItemHistory item = new ItemHistory();

                    item.Id = long.Parse(sdr["ID"].ToString());
                    item.Name = sdr["NAME"].ToString();
                    item.Quantity = float.Parse(sdr["QUANTITY"].ToString());
                    item.Unit = sdr["UNIT"].ToString();
                    item.Username = username;
                    item.Date = sdr["DATE"].ToString();

                    history.Add(item);
                }
            }

            connection.Close();

            return history;
        }

        public static bool ClearHistory(string itemName, string username)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd;

            if (string.IsNullOrEmpty(itemName))
            {
                cmd = new SqlCommand("delete from ITEMHISTORY where USERNAME=@username", connection.con);
            }
            else
            {
                cmd = new SqlCommand("delete from ITEMHISTORY where NAME=@name AND USERNAME=@username", connection.con);
                cmd.Parameters.Add(new SqlParameter("@name", itemName));
            }

            cmd.Parameters.Add(new SqlParameter("@username", username));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static List<string> GetItemList(string username)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List<string> list = new List<string>();

            SqlCommand cmd = new SqlCommand("SELECT DISTINCT NAME FROM ITEMHISTORY WHERE USERNAME=@username", connection.con);
            cmd.Parameters.Add(new SqlParameter("@username", username));

            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                while (sdr.Read())
                {
                    list.Add(sdr[0].ToString());
                }
            }

            connection.Close();

            return list;
        }

        internal static List<string> GetItemHistoryYears(User user, string itemName)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List<string> list = new List<string>();

            SqlCommand cmd = new SqlCommand("SELECT DISTINCT YEAR(CONVERT(DATE,[Date],103)) FROM ItemHistory WHERE NAME=@name AND [USERNAME]=@username", connection.con);
            cmd.Parameters.Add(new SqlParameter("@username", user.Username));
            cmd.Parameters.Add(new SqlParameter("@name", itemName));

            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                while (sdr.Read())
                {
                    list.Add(sdr[0].ToString());
                }
            }

            connection.Close();

            return list;
        }
    }
}
