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
        #region user
        public string Register(User user)
        {
            if (!UserSet.IsExists(user.Username))
            {
                return UserSet.AddUser(user.Username, user.Password);
            }

            return "-1";
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
        #endregion

        #region template

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
        #endregion

        #region notification

        public List<Notification> GetNotifications(User user)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.GetNotifications(user.Username);
            }
            return null;
        }

        public string GetNotificationsCount(User user)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.CountNotifications(user.Username).ToString();
            }

            return "-1";
        }

        public bool SetNotificationAsRead(User user, int notificationId)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.SetNotificationRead(new Notification() { Id = notificationId, Username = user.Username });
            }

            return false;
        }

        public bool UpdateNotification(User user, int notificationId, string text)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.UpdateNotification(user.Username, notificationId, text);
            }

            return false;
        }

        public bool DeleteNotification(User user, int notificationId)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.DeleteNotification(new Notification() { Id = notificationId, Username = user.Username });
            }

            return false;
        }
        #endregion

        #region ItemHistory
        public List<string> GetItems(User user)
        {
            if (IsValidUser(user))
            {
                return ItemHistorySet.GetItemList(user.Username);
            }
            return null;
        }

        public List<ItemHistory> GetItemHistory(User user, string itemName)
        {
            if (IsValidUser(user))
            {
                return ItemHistorySet.GetItemHistory(itemName, user.Username);
            }
            return null;
        }

        public bool ClearItemHistory(User user, string itemName)
        {
            if (IsValidUser(user))
            {
                return ItemHistorySet.ClearHistory(itemName, user.Username);
            }
            return false;
        }

        #endregion

        #region ListItem
        public List<ListItem> GetListItems(User user, string listId)
        {
            if (IsValidUser(user)) {
                return ListItemSet.GetListItems(long.Parse(listId));
            }
            return null;
        }

        public int GetListItemsCount(User user, string listId)
        {
            if(IsValidUser(user))
            {
                return ListItemSet.CountListItems(listId);
            }
            return -1;
        }

        public string AddListItem(User user, ListItem listItem)
        {
            if (IsValidUser(user))
            {
                ListItemSet.AddListItem(listItem);
            }
            return "-1";
        }

        public bool UpdateListItem(User user, ListItem listItem)
        {
            if (IsValidUser(user))
            {
                return ListItemSet.UpdateListItem(listItem);
            }
            return false;
        }

        public bool DeleteListItem(User user, string itemId, string time)
        {
            if (IsValidUser(user))
            {
                return ListItemSet.DeleteListItem(int.Parse(itemId), time);
            }

            return false;
        }
        #endregion
    }
}
