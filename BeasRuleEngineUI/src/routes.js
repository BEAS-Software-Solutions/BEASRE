

import Icon from "@mui/material/Icon";
import LoginUI from "layouts/login";
import LogoutUI from "layouts/logout";
import Callback from "layouts/callback";
import RulesLayout from "layouts/rules";
import FunctionLayout from "layouts/functions";
import HelperLayout from "layouts/helpers";
import SettingsLayout from "layouts/settings";

const routes = [
  {
    menu: true,
    type: "collapse",
    name: "Rules",
    key: "rules",
    icon: <Icon fontSize="small">dashboard</Icon>,
    route: "/rules",
    component: <RulesLayout />,
  },
  {
    menu: true,
    type: "collapse",
    key: "functions",
    route: "/functions",
    name: "Functions",
    icon: <Icon fontSize="small">account_tree</Icon>,
    component: <FunctionLayout />,
  },
  {
    menu: true,
    type: "collapse",
    key: "helpers",
    route: "/helpers",
    name: "Helpers",
    icon: <Icon fontSize="small">class</Icon>,
    component: <HelperLayout />,
  },
  {
    menu: true,
    type: "collapse",
    key: "settings",
    route: "/settings",
    name: "Settings",
    icon: <Icon fontSize="small">settings</Icon>,
    component: <SettingsLayout />,
  },
  {
    whiteListed: true,
    menu: false,
    type: "collapse",
    name: "callback",
    key: "callback",
    icon: <Icon fontSize="small">assignment</Icon>,
    route: "/callback",
    component: <Callback />,
  },
  {
    whiteListed: true,
    menu: false,
    type: "collapse",
    name: "logout",
    key: "logout",
    icon: <Icon fontSize="small">exit</Icon>,
    route: "/logout",
    component: <LogoutUI />,
  },
  {
    whiteListed: true,
    menu: false,
    type: "collapse",
    name: "login",
    key: "login",
    icon: <Icon fontSize="small">exit</Icon>,
    route: "/login",
    component: <LoginUI />,
  },
];

export default routes;
