 $(document).ready(function () {
            $(".page-link").on("click", function () {
                $("#page").val($(this).data("page"));
                $("#searchForm").submit();
            });

            $("#btn_search").on("click", function () {
                $("#kw").val($("#search_kw").val());
                $("#page").val(0);
                $("#searchForm").submit();
            });
        });