using SmartGroceryApiLibrary.DataAccess;
using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace SmartGroceryApiLibrary
{
    public class SmartGroceryApis : ISmartGroceryApis
    {
        public string Register(User user)
        {
            if (!UserSet.IsExists(user.Username))
            {
                return UserSet.AddUser(user.Username, user.Password);
            }

            return null;
        }

        public string Authenticate(string authKey)
        {
            return UserSet.Authenticate(authKey);
        }

        public string GetAuthKey(User user)
        {
            return UserSet.GetAuthKey(user.Username, user.Password);
        }

        public bool UpdatePassword(User user, string newPassword)
        {
            return UserSet.UpdatePassword(user.Username, user.Password, newPassword);
        }

        public bool Delete(User user)
        {
            if (IsValidUser(user))
            {
                return UserSet.RemoveUser(user.Username, user.Password);
            }

            return false;

        }

        private bool IsValidUser(User user)
        {
            string username = UserSet.GetUsername(user.SESS_KEY);
            if (!string.IsNullOrEmpty(username) && username.Equals(user.Username, StringComparison.CurrentCulture))
            {
                return true;
            }

            return false;
        }


        public List<Template> GetTemplates(string query)
        {
            return TemplateSet.GetTemplateList(query);
        }

        public Template GetTemplate(string templateId)
        {
            return TemplateSet.GetTemplate(int.Parse(templateId));
        }

        public List<TemplateItem> GetTemplateItems(string templateId)
        {
            return TemplateItemSet.GetTemplateItems(int.Parse(templateId));
        }
    }
}
