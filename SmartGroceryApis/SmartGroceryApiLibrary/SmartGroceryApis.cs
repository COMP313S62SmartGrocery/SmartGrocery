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
        public bool Register(string username, string password)
        {
            throw new NotImplementedException();
        }

        public bool GetKey(string username, string password)
        {
            throw new NotImplementedException();
        }

        public bool UpdatePassword(string username, string password, string newPassword)
        {
            throw new NotImplementedException();
        }

        public bool Delete(string username, string password, string newPassword)
        {
            throw new NotImplementedException();
        }

        public bool GetLists(List<List> list)
        {
            throw new NotImplementedException();
        }

        public bool AddList(List list)
        {
            throw new NotImplementedException();
        }

        public bool DeleteList(long listId)
        {
            throw new NotImplementedException();
        }


        public List<Template> GetTemplates(string query, string sessionKey)
        {
            TemplateSet set = new TemplateSet();
            return set.GetTemplateList(query);
        }

        public List<TemplateItem> GetTemplate(string templateId, string sessionKey)
        {
            throw new NotImplementedException();
        }

        public bool AddList(List list, string sessionKey)
        {
            throw new NotImplementedException();
        }

        public bool DeleteList(long listId, string sessionKey)
        {
            throw new NotImplementedException();
        }

        public bool DuplicateList(long listId, string sessionKey)
        {
            throw new NotImplementedException();
        }

        public bool RenameList(long listId, string newName, string sessionKey)
        {
            throw new NotImplementedException();
        }

        public bool RenameList(ListItem listItem, string sessionKey)
        {
            throw new NotImplementedException();
        }

        public bool Register(User user)
        {
            throw new NotImplementedException();
        }

        public bool GetKey(User user)
        {
            throw new NotImplementedException();
        }

        public bool UpdatePassword(User user, string newPassword)
        {
            throw new NotImplementedException();
        }

        public bool Delete(User user)
        {
            throw new NotImplementedException();
        }
    }
}
