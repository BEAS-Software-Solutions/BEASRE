

import { useState, useEffect, useRef } from "react";

// react-router components
import { Routes, Route, Navigate, useLocation } from "react-router-dom";

// @mui material components
import { ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import Icon from "@mui/material/Icon";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";

// Material Dashboard 2 React example components
import Sidenav from "widgets/Sidenav";
import Configurator from "widgets/Configurator";

// Material Dashboard 2 React themes
import theme from "assets/theme";

// Material Dashboard 2 React Dark Mode themes
import themeDark from "assets/theme-dark";

// Material Dashboard 2 React routes
import routes from "routes";

// Material Dashboard 2 React contexts
import { useMaterialUIController, setMiniSidenav, setOpenConfigurator } from "context";

// Images
import brandWhite from "assets/images/beaslogo-landscape-white.webp"; //logo-ct.png";
import brandDark from "assets/images/beaslogo-landscape-black.webp"; //logo-ct-dark.png";
import { ConfirmProvider } from "material-ui-confirm";
import { setGlobalErrorHandler } from "api/api";
import ErrorModal from "components/Error";
import Notification from "components/Notification";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import 'App.css';
import authService from "services/authService";
import ProtectedRoute from "services/ProtectedRoute";


export default function App() {
  const [loading, setLoading] = useState(false);
  const brandName = "RULE ENGINE AS A SERVICE";
  const [controller, dispatch] = useMaterialUIController();
  const {
    miniSidenav,
    direction,
    layout,
    openConfigurator,
    sidenavColor,
    transparentSidenav,
    whiteSidenav,
    darkMode,
  } = controller;
  const [onMouseEnter, setOnMouseEnter] = useState(false);
  const { pathname } = useLocation();
  const [user, setUser] = useState(null);

  const [isModalOpen, setModalOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const notificationElement = useRef();

  const showSnack = async (opts) => {
    new Promise((resolve) => {
      if (notificationElement.current) {
        notificationElement.current.show(opts);
        resolve(opts);
      }
    });
  };

  useEffect(() => {
    const handleLoading = (event) => {
      setLoading(event.detail);
    };

    window.addEventListener('loading', handleLoading);


    setGlobalErrorHandler((e) => {
      const opts = {
        color: "error",
        icon: "error",
        title: "Unexpected Error",
        reason: "For Page Execution",
        content: "Unexpected Error",
        info: "Unexpected Error",
      };
      if (e.data) {
        opts.title = "Error";
        opts.content = e.data.response.message;
        opts.reason = e.data.response.reason;
        opts.info = e.data.response.errorCode;
      } else {
        opts.title = "Error";
        opts.reason = "Unexpected Error";
        opts.content = e;
        opts.info = "Unexpected Error";
      }

      showSnack(opts);
    });

    authService.getUser().then((loadedUser) => {
      if (loadedUser) {
        setUser(loadedUser);
      }
    });

    return () => {
      window.removeEventListener('loading', handleLoading);
    };
  }, []);

  // Modal'ı kapatma fonksiyonu
  const handleCloseModal = () => {
    setModalOpen(false);
    setErrorMessage('');
  };

  // Open sidenav when mouse enter on mini sidenav
  const handleOnMouseEnter = () => {
    if (miniSidenav && !onMouseEnter) {
      setMiniSidenav(dispatch, false);
      setOnMouseEnter(true);
    }
  };

  // Close sidenav when mouse leave mini sidenav
  const handleOnMouseLeave = () => {
    if (onMouseEnter) {
      setMiniSidenav(dispatch, true);
      setOnMouseEnter(false);
    }
  };

  // Change the openConfigurator state
  const handleConfiguratorOpen = () => setOpenConfigurator(dispatch, !openConfigurator);

  // Setting the dir attribute for the body element
  useEffect(() => {
    document.body.setAttribute("dir", direction);
  }, [direction]);

  // Setting page scroll to 0 when changing the route
  useEffect(() => {
    document.documentElement.scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
  }, [pathname]);

  const getRoutes = (allRoutes) =>
    allRoutes.map((route) => {
      if (route.collapse) {
        return getRoutes(route.collapse);
      }

      if (route.route) {
        return route?.whiteListed ? (
          <Route exact path={route.route} element={route.component} key={route.key} />
        ) : (
          <Route exact path={route.route} element={
            <ProtectedRoute user={user}>
              {route.component}
            </ProtectedRoute>
          } key={route.key} />
        );
      }

      return null;
    });

  const configsButton = (
    <MDBox
      display="flex"
      justifyContent="center"
      alignItems="center"
      width="3.25rem"
      height="3.25rem"
      bgColor="white"
      shadow="sm"
      borderRadius="50%"
      position="fixed"
      right="2rem"
      bottom="2rem"
      zIndex={99}
      color="dark"
      sx={{ cursor: "pointer" }}
      onClick={handleConfiguratorOpen}
    >
      <Icon fontSize="small" color="inherit">
        settings
      </Icon>
    </MDBox>
  );

  return (

    <ThemeProvider theme={darkMode ? themeDark : theme}>
      {loading && (
        <div className="spinner-overlay">
          <div className="spinner"></div>
        </div>
      )}
      <Notification ref={notificationElement} />
      <ErrorModal
        open={isModalOpen}
        handleClose={handleCloseModal}
        message={errorMessage}
      />
      <CssBaseline />
      {layout === "dashboard" && (
        <>
          <Sidenav
            color={sidenavColor}
            brand={(transparentSidenav && !darkMode) || whiteSidenav ? brandDark : brandWhite}
            brandName={brandName}
            routes={routes}
            onMouseEnter={handleOnMouseEnter}
            onMouseLeave={handleOnMouseLeave}
          />
          <Configurator />
          {configsButton}
        </>
      )}
      {layout === "vr" && <Configurator />}
      <ConfirmProvider
        defaultOptions={{
          title: 'Are you sure?',
          confirmationText: 'Confirm',
          cancellationText: 'Cancel',
          dialogProps: {
            maxWidth: 'xs',
            fullWidth: true,
          },
        }}
      >
        <Routes>
          {getRoutes(routes)}
          <Route path="*" element={<Navigate to="/rules" />} />
        </Routes>
      </ConfirmProvider>
      <ToastContainer
        position="top-center"
        autoClose={3000}
        newestOnTop
        closeOnClick
        pauseOnFocusLoss
        pauseOnHover />

    </ThemeProvider>

  );
}

