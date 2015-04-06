using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.DataAccess
{
    public class TemplateItemSet
    {
        public static bool AddTemplateItem(TemplateItem templateItem)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("insert into TemplateItems(NAME,QUANTITY,UNIT,TEMPLATEID) values(@name,@quantity,@unit,@templateId)", connection.con);
            cmd.Parameters.Add(new SqlParameter("@name", templateItem.Name));
            cmd.Parameters.Add(new SqlParameter("@quantity", templateItem.Quantity));
            cmd.Parameters.Add(new SqlParameter("@unit", templateItem.Unit));
            cmd.Parameters.Add(new SqlParameter("@templateId", templateItem.TemplateId));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static TemplateItem GetTemplateItem(long id)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            TemplateItem templateItem = null;

            SqlCommand cmd = new SqlCommand("Select * from TemplateItems where Id=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", id));
            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                templateItem = new TemplateItem();
                templateItem.Id = id;
                templateItem.Name = sdr["NAME"].ToString();
                templateItem.Quantity = float.Parse(sdr["QUANTITY"].ToString());
                templateItem.Unit = sdr["UNIT"].ToString();
                templateItem.TemplateId = long.Parse(sdr["TemplateId"].ToString());
            }

            sdr.Close();
            connection.Close();

            return templateItem;
        }

        public static bool DeleteTemplateItem(long templateItemId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            SqlCommand cmd = new SqlCommand("delete from TemplateItems where ID=@id", connection.con);
            cmd.Parameters.Add(new SqlParameter("@id", templateItemId));

            bool ret = cmd.ExecuteNonQuery() > 0;

            connection.Close();

            return ret;
        }

        public static List<TemplateItem> GetTemplateItems(int templateId)
        {
            ConnectionManager connection = new ConnectionManager();
            connection.Open();

            List<TemplateItem> list = new List<TemplateItem>();

            SqlCommand cmd = new SqlCommand("Select * from TemplateItems where TemplateId=@templateId", connection.con);
            cmd.Parameters.Add(new SqlParameter("@templateId", templateId));

            SqlDataReader sdr = cmd.ExecuteReader();

            if (sdr.HasRows)
            {
                while (sdr.Read())
                {
                    TemplateItem templateItem = new TemplateItem();
                    templateItem.Id = long.Parse(sdr["ID"].ToString());
                    templateItem.Name = sdr["NAME"].ToString();
                    templateItem.Quantity = float.Parse(sdr["QUANTITY"].ToString());
                    templateItem.Unit = sdr["UNIT"].ToString();
                    templateItem.TemplateId = templateId;

                    list.Add(templateItem);
                }
            }

            sdr.Close();
            connection.Close();

            return list;
        }
    }
}
