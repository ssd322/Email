// =========================================================
// send-email.js - Memicu pengiriman email & polling progress realtime
// =========================================================

document.addEventListener("DOMContentLoaded", function () {
    const btnKirim = document.getElementById("btnKirim");
    if (!btnKirim) return;

    const delayInput = document.getElementById("delayInput");
    const loadingIndicator = document.getElementById("loadingIndicator");
    const progressBar = document.getElementById("progressBar");
    const progressText = document.getElementById("progressText");
    const countSukses = document.getElementById("countSukses");
    const countGagal = document.getElementById("countGagal");
    const statusTableBody = document.getElementById("statusTableBody");
    const emptyRow = document.getElementById("emptyRow");
    const statStatus = document.getElementById("statStatus");

    let renderedLogCount = 0;
    let pollingInterval = null;

    function setSendingState(isSending) {
        btnKirim.disabled = isSending;
        loadingIndicator.classList.toggle("d-none", !isSending);
        statStatus.textContent = isSending ? "Mengirim..." : "Siap Mengirim";
        statStatus.className = isSending ? "fw-bold mb-0 text-primary" : "fw-bold mb-0";
    }

    function appendLogRow(logEntry) {
        const parts = logEntry.split("|");
        const type = parts[0];
        const nama = parts[1] || "-";
        const email = parts[2] || "-";
        const pesan = parts[3] || "-";

        if (emptyRow) {
            emptyRow.remove();
        }

        const tr = document.createElement("tr");
        tr.className = type === "OK" ? "status-sent" : "status-failed";

        const badgeClass = type === "OK" ? "bg-success" : "bg-danger";
        const badgeText = type === "OK" ? "Berhasil" : "Gagal";
        const icon = type === "OK" ? "bi-check-circle-fill" : "bi-x-circle-fill";

        tr.innerHTML =
            "<td>" + escapeHtml(nama) + "</td>" +
            "<td>" + escapeHtml(email) + "</td>" +
            "<td><span class='badge " + badgeClass + "'><i class='bi " + icon + "'></i> " + badgeText + "</span></td>" +
            "<td class='small text-muted'>" + escapeHtml(pesan) + "</td>";

        statusTableBody.prepend(tr);
    }

    function escapeHtml(text) {
        const div = document.createElement("div");
        div.textContent = text;
        return div.innerHTML;
    }

    function updateProgressUI(data) {
        progressBar.style.width = data.percentage + "%";
        progressBar.textContent = data.percentage + "%";
        progressText.textContent = data.processed + " / " + data.total + " (" + data.percentage + "%)";
        countSukses.textContent = data.sent;
        countGagal.textContent = data.failed;

        if (data.logs && data.logs.length > renderedLogCount) {
            const newLogs = data.logs.slice(renderedLogCount);
            newLogs.forEach(appendLogRow);
            renderedLogCount = data.logs.length;
        }

        if (!data.running && data.finished) {
            stopPolling();
            setSendingState(false);
            showResultAlert(data);
        }
    }

    function showResultAlert(data) {
        const container = document.querySelector(".container-fluid");
        const alertType = data.failed > 0 ? "alert-warning" : "alert-success";
        const icon = data.failed > 0 ? "bi-exclamation-triangle-fill" : "bi-check-circle-fill";

        const alertDiv = document.createElement("div");
        alertDiv.className = "alert " + alertType + " alert-dismissible fade show";
        alertDiv.innerHTML =
            "<i class='bi " + icon + " me-1'></i> Pengiriman selesai. Berhasil: " + data.sent +
            ", Gagal: " + data.failed + "." +
            "<button type='button' class='btn-close' data-bs-dismiss='alert'></button>";

        container.prepend(alertDiv);

        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alertDiv);
            if (bsAlert) bsAlert.close();
        }, 6000);
    }

    function pollProgress() {
        fetch("/kirim-email/progress")
            .then((res) => res.json())
            .then(updateProgressUI)
            .catch((err) => console.error("Gagal mengambil progress:", err));
    }

    function startPolling() {
        pollingInterval = setInterval(pollProgress, 1000);
    }

    function stopPolling() {
        if (pollingInterval) {
            clearInterval(pollingInterval);
            pollingInterval = null;
        }
    }

    btnKirim.addEventListener("click", function () {
        if (!confirm("Mulai mengirim email ke seluruh penerima valid?")) {
            return;
        }

        renderedLogCount = 0;
        statusTableBody.innerHTML = "";
        progressBar.style.width = "0%";
        progressBar.textContent = "0%";
        progressText.textContent = "0 / 0 (0%)";
        countSukses.textContent = "0";
        countGagal.textContent = "0";
        setSendingState(true);

        const delayMs = delayInput.value || 1000;
        const formData = new URLSearchParams();
        formData.append("delayMs", delayMs);

        fetch("/kirim-email/mulai", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: formData.toString()
        })
            .then((res) => res.json())
            .then(function () {
                startPolling();
            })
            .catch(function (err) {
                console.error("Gagal memulai pengiriman:", err);
                setSendingState(false);
            });
    });
});
