// =========================================================
// template.js - Live preview subject & isi HTML template email
// =========================================================

document.addEventListener("DOMContentLoaded", function () {
    const subjectInput = document.getElementById("subjectInput");
    const htmlContentInput = document.getElementById("htmlContentInput");
    const previewSubject = document.getElementById("previewSubject");
    const previewContent = document.getElementById("previewContent");
    const btnRefreshPreview = document.getElementById("btnRefreshPreview");

    function renderPreview() {
        const sampleName = "Budi Santoso";
        const sampleEmail = "budi.santoso@example.com";

        previewSubject.textContent = subjectInput.value || "(Belum ada subject)";

        const rawHtml = htmlContentInput.value || "<p class='text-muted'>(Belum ada isi email)</p>";
        const personalized = rawHtml
            .replaceAll("{{nama}}", sampleName)
            .replaceAll("{{email}}", sampleEmail);

        previewContent.innerHTML = personalized;
    }

    if (subjectInput && htmlContentInput) {
        subjectInput.addEventListener("input", renderPreview);
        htmlContentInput.addEventListener("input", renderPreview);
        btnRefreshPreview.addEventListener("click", renderPreview);

        // Render pertama kali saat halaman dimuat
        renderPreview();
    }
});
