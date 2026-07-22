// =========================================================
// main.js - Perilaku umum UI (sidebar toggle, auto-dismiss alert)
// =========================================================

document.addEventListener("DOMContentLoaded", function () {
    // Toggle sidebar pada layar kecil (mobile responsive)
    const sidebarToggle = document.getElementById("sidebarToggle");
    const sidebar = document.querySelector(".sidebar");

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener("click", function () {
            sidebar.classList.toggle("show");
        });

        document.addEventListener("click", function (event) {
            const isClickInsideSidebar = sidebar.contains(event.target);
            const isClickOnToggle = sidebarToggle.contains(event.target);
            if (!isClickInsideSidebar && !isClickOnToggle && window.innerWidth < 992) {
                sidebar.classList.remove("show");
            }
        });
    }

    // Auto-dismiss alert Bootstrap setelah 5 detik
    document.querySelectorAll(".alert").forEach(function (alertEl) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alertEl);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 5000);
    });
});
